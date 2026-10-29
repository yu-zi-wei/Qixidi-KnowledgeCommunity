<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="清单名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入清单名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="完成地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入地址"
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
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          size="small"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
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
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="repertoireList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id" v-if="true"/>
      <el-table-column label="清单名称" align="center" prop="name"/>
      <el-table-column label="清单内容" align="center" prop="content">
        <template slot-scope="scope">
          <el-tooltip content="点击查看详情" placement="top">
            <div class="content-click" @click="contentDetails(scope.row.id)">
              <span>查看详情></span>
            </div>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="state">
        <template slot-scope="scope">
          <el-tooltip :content="''+scope.row.state==0?'已完成':'待完成'+''" placement="top">
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
      <!--      <el-table-column label="排序" align="center" prop="order"/>-->
      <el-table-column label="完成地址" align="center" prop="address"/>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180"
                       sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="完成时间" align="center" prop="createTime" width="180"
                       sortable="custom">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}') }}</span>
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

    <!-- 添加或修改爱情清单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="清单名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入清单名称" maxlength="40" show-word-limit/>
        </el-form-item>
        <el-form-item label="清单内容">
          <editor v-model="form.content" :height="400"/>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="地址">
              <el-input v-model="form.address" placeholder="请输入地址" maxlength="40" show-word-limit/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="state">
              <el-radio-group v-model="form.state">
                <el-radio :label="0">已完成</el-radio>
                <el-radio :label="1">待完成</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="创建时间" prop="createTime">
              <el-date-picker clearable
                              v-model="form.createTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择创建时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.state==0">
            <el-form-item label="完成时间" prop="updateTime">
              <el-date-picker clearable
                              v-model="form.updateTime"
                              type="datetime"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              placeholder="请选择完整时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-drawer
      :title="detailsTitle"
      size="100%"
      :visible.sync="detailsLoading"
      direction="ttb">
      <div style="padding: 10px 40px;">
        <div class="content-cl-ct" v-html="detailsText"></div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import {listRepertoire, getRepertoire, delRepertoire, addRepertoire, updateRepertoire} from "@/api/lover/repertoire";

export default {
  name: "Repertoire",
  data() {
    return {
      daterangeCreateTime: [],
      states: [
        {value: 0, label: '未完成'},
        {value: 1, label: '已完成'},
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
      // 爱情清单表格数据
      repertoireList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        content: undefined,
        img: undefined,
        state: undefined,
        order: undefined,
        address: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          {required: true, message: "清单名称不能为空", trigger: "blur"}
        ],
        state: [
          {required: true, message: "状态不能为空", trigger: "blur"}
        ],
        createTime: [
          {required: true, message: "创建时间不能为空", trigger: "blur"}
        ],
        updateTime: [
          {required: true, message: "完成时间不能为空", trigger: "blur"}
        ],
      },
      detailsLoading: false,
      detailsText: '',
      detailsTitle: '',
    };
  },
  created() {
    this.getList();
  },
  methods: {
    contentDetails(id) {
      this.loading = true;
      getRepertoire(id).then(response => {
        this.detailsLoading = true;
        this.detailsText = response.data.content;
        this.detailsTitle = response.data.name;
        this.loading = false;
      }).finally(() => this.loading = false);
    },
    updateState(item) {
      this.$modal.confirm('是否确认修改爱情清单编号为"' + item.id + '"的数据项？').then(() => {
        this.loading = true;
        return updateRepertoire(item);
      }).then(() => {
        this.loading = false;
        this.$modal.msgSuccess("修改成功");
      }).finally(() => {
        this.getList();
        this.loading = false;
      });
    },
    /** 查询爱情清单列表 */
    getList() {
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }

      this.loading = true;
      listRepertoire(this.queryParams).then(response => {
        this.repertoireList = response.rows;
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
        name: undefined,
        content: undefined,
        img: undefined,
        state: undefined,
        order: undefined,
        createTime: undefined,
        updateTime: undefined,
        address: undefined,
        createBy: undefined,
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
      this.title = "添加爱情清单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getRepertoire(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改爱情清单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateRepertoire(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addRepertoire(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除爱情清单编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delRepertoire(ids);
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
      this.download('lover/repertoire/export', {
        ...this.queryParams
      }, `repertoire_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style>
.content-cl-ct {
  text-align: center;
}

.content-cl-ct img {
  max-width: 60%;
  max-height: 400px;
  padding: 20px;
  object-fit: cover;
  object-position: left top;
  transition: 1s;
}

.content-cl-ct img:hover {
  object-position: right bottom;
  transform: scale(1.1);
}
</style>
