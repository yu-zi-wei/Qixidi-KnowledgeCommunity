<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="网站标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入网站标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-select v-model="queryParams.state" placeholder="请选择">
          <el-option
            v-for="item in states"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择">
          <el-option
            v-for="dict in dict.type.lover_data_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
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
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-view"
          size="mini"
        >
          <router-link to="/system/dict-data/index/1596777517165584385">
            查看类型
          </router-link>
        </el-button>
        <span class="color-error ml-8">
          <i class="el-icon-info"></i>
          提示：前台默认失效一条数据，如果存在多条（状态）正常数据，默认按类型取靠前的第一条
        </span>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" align="center" prop="id" v-if="true"/>
      <el-table-column label="网站标题" align="center" prop="title"/>
      <el-table-column label="爱情句子" align="center" prop="introduce"/>
      <el-table-column label="左侧名称" align="center" prop="leftName"/>
      <el-table-column label="左侧头像" align="center" prop="leftImg">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.leftImg"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column label="右侧名称" align="center" prop="rightName"/>
      <el-table-column label="右侧头像" align="center" prop="rightImg">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.rightImg"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="state">
        <template slot-scope="scope">
          <el-tooltip :content="''+scope.row.state==0?'正常':'未启用'+''" placement="top">
            <el-switch
              v-model="scope.row.state"
              active-color="#13ce66"
              :active-value="0"
              @change="updateState(scope.row)"
              :inactive-value="1"
              inactive-color="#ff4949">
            </el-switch>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="type">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.lover_data_type" :value="scope.row.type"/>
        </template>
      </el-table-column>
      <el-table-column label="恋爱开始时间" align="center" prop="updateTime" width="180"
                       sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.loverTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-document-checked"
            @click="handleCopy(scope.row)"
          >复制
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
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

    <!-- 添加或修改网站基本信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="网站标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入网站标题" maxlength="20" show-word-limit/>
        </el-form-item>
        <el-form-item label="爱情句子" prop="introduce">
          <el-input type="textarea" maxlength="200"
                    show-word-limit rows="3" v-model="form.introduce" placeholder="爱情句子..."/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="左侧头像" prop="leftImg">
              <div class="text-center fl-left">
                <Avatar :file.sync="form.leftImg" :avatarImg="form.leftImg"/>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="右侧头像" prop="rightImg">
              <div class="text-center fl-left">
                <Avatar :file.sync="form.rightImg" :avatarImg="form.rightImg"/>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="左侧名称" prop="leftName">
              <el-input v-model="form.leftName" placeholder="请输入左名称" maxlength="20" show-word-limit/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="右侧名称" prop="rightName">
              <el-input v-model="form.rightName" placeholder="请输入右名称" maxlength="20" show-word-limit/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="恋爱前缀" prop="loverPrefix">
              <el-input v-model="form.loverPrefix" placeholder="这是我们在一起的" maxlength="40" show-word-limit/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="恋爱时间" prop="loverTime">
              <el-date-picker
                v-model="form.loverTime"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择">
                <el-option
                  v-for="dict in dict.type.lover_data_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="state">
              <el-radio-group v-model="form.state">
                <el-radio :label="0">启用</el-radio>
                <el-radio :label="1">不启用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listInfo, getInfo, delInfo, addInfo, updateInfo} from "@/api/lover/basic";
import Avatar from "@/components/Avatar/updateAvatar"

export default {
  dicts: ['lover_data_type'],
  name: "Info",
  components: {
    Avatar
  },
  data() {
    return {
      //状态
      states: [
        {value: 0, label: '正常'},
        {value: 1, label: '已停用'},
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
      // 网站基本信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        introduce: undefined,
        leftImg: undefined,
        leftName: undefined,
        rightImg: undefined,
        rightName: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          {required: true, message: "网站标题不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "数据类型不能为空", trigger: "blur"}
        ],
        state: [
          {required: true, message: "数据状态不能为空", trigger: "blur"}
        ],
        loverPrefix: [
          {required: true, message: "恋爱前缀不能为空", trigger: "blur"}
        ],
        loverTime: [
          {required: true, message: "恋爱时间不能为空", trigger: "blur"}
        ],
      },

    };
  },
  created() {
    this.getList();
  },
  methods: {
    updateState(item) {
      this.$modal.confirm('是否确认修改网站信息编号为"' + item.id + '"的数据项？').then(() => {
        this.loading = true;
        return updateInfo(item);
      }).then(() => {
        this.loading = false;
        this.$modal.msgSuccess("修改成功");
      }).finally(() => {
        this.getList();
        this.loading = false;
      });
    },
    /** 查询网站基本信息列表 */
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
        title: undefined,
        introduce: undefined,
        leftImg: undefined,
        leftName: undefined,
        rightImg: undefined,
        rightName: undefined,
        createTime: undefined,
        createBy: undefined,
        updateTime: undefined,
        updateBy: undefined
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
      this.title = "添加网站基本信息";
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
        this.title = "修改网站基本信息";
      });
    },
    handleCopy(item) {
      this.$modal.confirm('是否确认复制基本信息编号为"' + item.id + '"的数据项？').then(() => {
        this.loading = true;
        item.id = null;
        return addInfo(item);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("复制成功");
      }).finally(() => {
        this.loading = false;
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
      this.$modal.confirm('是否确认删除网站基本信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delInfo(ids);
      }).then(() => {
        this.loading = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      }).finally(() => {
        this.loading = false;
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('basic/info/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

