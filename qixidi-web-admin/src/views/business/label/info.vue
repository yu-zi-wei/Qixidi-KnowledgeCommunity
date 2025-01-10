<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="分组名称" prop="groupingName">
        <el-select v-model="queryParams.groupingName" placeholder="请选择">
          <el-option
            v-for="item in groupingInfoList"
            :key="item.groupingName"
            :label="item.groupingName"
            :value="item.groupingName">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="名称" prop="labelName">
        <el-input
          v-model="queryParams.labelName"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-input
          v-model="queryParams.state"
          placeholder=""
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['business:label-info:add']"
        >新增
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
          v-hasPermi="['business:label-info:remove']"
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
          v-hasPermi="['business:label-info:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id" v-if="true"/>
      <el-table-column label="分组名称" align="center" prop="groupingName"/>
      <el-table-column label="名称" align="center" prop="labelName"/>
      <el-table-column label="描述" align="center" prop="labelDescribe"/>
      <el-table-column label="图标" align="center" prop="labelCover">
        <template v-slot="scope">
          <span v-html="scope.row.labelCover"></span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="state">
        <template v-slot="scope">
          <dict-tag :options="dict.type.state_type" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:label-info:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:label-info:remove']"
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

    <!-- 添加或修改标签信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="分组名称" prop="labelGroupingId">
          <el-select v-model="form.labelGroupingId" placeholder="请选择">
            <el-option
              v-for="item in groupingInfoList"
              :key="item.id"
              :label="item.groupingName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称" prop="labelName">
          <el-input v-model="form.labelName" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="描述" prop="labelDescribe">
          <el-input v-model="form.labelDescribe" placeholder="请输入描述"/>
        </el-form-item>
        <el-form-item label="图标" prop="labelCover">
          <el-input type="textarea" :rows="6" v-model="form.labelCover" placeholder="请输入图标"/>
        </el-form-item>
        <el-form-item label="图标展示" prop="navigationIcon">
          <span v-html="form.labelCover"></span>
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
import {listInfo, getInfo, delInfo, addInfo, updateInfo} from "@/api/business/label/info";
import {listGroupingInfo} from "@/api/business/label/groupingInfo";

export default {
  name: "Info",
  dicts: ['state_type'],
  data() {
    return {
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
      // 标签信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        labelGroupingId: undefined,
        labelName: undefined,
        labelDescribe: undefined,
        labelCover: undefined,
        state: undefined,
        businessType: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        labelGroupingId: [
          {required: true, message: "分组id不能为空", trigger: "blur"}
        ],
        labelName: [
          {required: true, message: "名称不能为空", trigger: "blur"}
        ],
      },
      groupingInfoList: [],
    };
  },
  created() {
    this.getList();
    this.getGroupList();
  },
  methods: {
    getGroupList() {
      listGroupingInfo().then(response => {
        this.groupingInfoList = response.rows;
      });
    },
    /** 查询标签信息列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
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
        labelGroupingId: undefined,
        labelName: undefined,
        labelDescribe: undefined,
        labelCover: undefined,
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
      this.title = "添加标签信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getInfo(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改标签信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除标签信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delInfo(ids);
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
      this.download('business/label-info/export', {
        ...this.queryParams
      }, `标签信息${new Date().getTime()}.xlsx`)
    }
  }
};

</script>
