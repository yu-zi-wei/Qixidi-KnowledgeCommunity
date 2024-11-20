<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="屏蔽字" prop="keyword">
        <el-input
          v-model="queryParams.keyword"
          placeholder="请输入屏蔽字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="权重" prop="weight">
        <el-input
          v-model="queryParams.weight"
          placeholder="请输入屏蔽词权重"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择" clearable>
          <el-option
            v-for="dict in dict.type.state_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择" clearable>
          <el-option
            v-for="item in typeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['shield:shieldWord:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['shield:shieldWord:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['shield:shieldWord:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['shield:shieldWord:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          :loading="buttonLoading"
          type="warning"
          plain
          icon="el-icon-refresh-right"
          size="mini"
          @click="cacheBlockingWordss"
          v-hasPermi="['shield:shieldWord:add']"
        >刷新配置
        </el-button>
      </el-col>
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          :loading="buttonLoading"-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-refresh-right"-->
<!--          size="mini"-->
<!--          @click="cacheBlockingWordsTexts"-->
<!--          v-hasPermi="['shield:shieldWord:add']"-->
<!--        >测试-->
<!--        </el-button>-->
<!--      </el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shieldWordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="屏蔽字" align="center" prop="keyword"/>
      <el-table-column label="类型" align="center" prop="type">
        <template v-slot="scope">
          <el-tag v-if="scope.row.type==1" type="info">文章</el-tag>
          <el-tag v-if="scope.row.type==2" type="warning">评论</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="屏蔽词权重" align="center" prop="weight"/>
      <el-table-column label="状态" align="center" prop="state">
        <template v-slot="scope">
          <dict-tag :options="dict.type.state_type" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['shield:shieldWord:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['shield:shieldWord:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改屏蔽词库对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="屏蔽字" prop="keyword">
          <el-input v-model="form.keyword" placeholder="请输入屏蔽字"/>
        </el-form-item>
        <el-form-item label="权重" prop="weight">
          <el-input v-model="form.weight" placeholder="请输入屏蔽词权重"/>
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择" clearable>
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listShieldWord,
  getShieldWord,
  delShieldWord,
  addShieldWord,
  updateShieldWord
} from "@/api/business/shield/shieldWord";
import {cacheBlockingWords,cacheBlockingWordsText} from "../../../api/business/cacheRefresh";

export default {
  name: "shieldWord",
  dicts: ['state_type'],
  data() {
    return {
      typeOptions: [
        {
          value: 1,
          label: '文章'
        }, {
          value: 2,
          label: '评论'
        }
      ],
      // 按钮loading
      buttonLoading: false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 屏蔽词库表格数据
      shieldWordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyword: undefined,
        type: undefined,
        weight: undefined,
        state: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        keyword: [
          {required: true, message: "屏蔽字不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "类型不能为空", trigger: "change"}
        ],
        weight: [
          {required: true, message: "屏蔽词权重不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    cacheBlockingWordsTexts(){
      cacheBlockingWordsText("傻逼打发打发 对方的 的的操尼玛")
    },
    cacheBlockingWordss() {
      this.buttonLoading = true;
      cacheBlockingWords().then(res => {
        this.$modal.msgSuccess("刷新成功");
      }).finally(() => this.buttonLoading = false);
    },
    /** 查询屏蔽词库列表 */
    getList() {
      this.loading = true;
      listShieldWord(this.queryParams).then(response => {
        this.shieldWordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        keyword: undefined,
        type: undefined,
        weight: undefined,
        state: undefined,
        createTime: undefined,
        createBy: undefined
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加屏蔽词库";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getShieldWord(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改屏蔽词库";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateShieldWord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addShieldWord(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除屏蔽池库编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delShieldWord(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('shield/shieldWord/export', {
        ...this.queryParams
      }, `shieldWord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
