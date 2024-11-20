<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="名称" prop="sidebarName">
        <el-input
          v-model="queryParams.sidebarName"
          placeholder="请输入侧边栏名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否下拉" prop="isList">
        <el-select v-model="queryParams.isList" placeholder="请选择" clearable>
          <el-option
            v-for="item in isListOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="路由地址" prop="route">
        <el-input
          v-model="queryParams.route"
          placeholder="请输入路由地址"
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
          v-hasPermi="['business:sidebar:add']"
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
          v-hasPermi="['business:sidebar:remove']"
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
          v-hasPermi="['business:sidebar:export']"
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
          @click="cacheSidebars"
          v-hasPermi="['business:sidebar:add']"
        >刷新配置
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sidebarList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id" v-if="true"/>
      <el-table-column label="侧边栏名称" align="center" prop="sidebarName"/>
      <el-table-column label="图标" align="center" prop="sidebarIcon">
        <template slot-scope="scope">
          <i v-html="scope.row.sidebarIcon"></i>
        </template>
      </el-table-column>
      <el-table-column label="排序" align="center" prop="order"/>
      <el-table-column label="是否有下拉" align="center" prop="isList">
        <template v-slot="scope">
          <el-tag :type="scope.row.isList==0? '':'warning'">
            {{ scope.row.isList == 0 ? '无下拉' : '有下拉' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="state">
        <template v-slot="scope">
          <dict-tag :options="dict.type.state_type" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="路由地址" align="center" prop="route"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:sidebar:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:sidebar:remove']"
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

    <!-- 添加或修改侧边栏配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="sidebarName">
          <el-input v-model="form.sidebarName" placeholder="请输入侧边栏名称"/>
        </el-form-item>
        <el-form-item label="图标" prop="sidebarIcon">
          <el-input v-model="form.sidebarIcon" placeholder="请输入图标"/>
        </el-form-item>
<!--        <el-form-item label="颜色" prop="iconColor">-->
<!--          <el-input v-model="form.iconColor" placeholder="请输入图标"/>-->
<!--        </el-form-item>-->
        <el-form-item label="图标展示" prop="sidebarIcon">
          <i v-html="form.sidebarIcon"></i>
        </el-form-item>
        <el-form-item label="排序" prop="order">
          <el-input v-model="form.order" placeholder="请输入排序"/>
        </el-form-item>
        <el-form-item label="是否下拉" prop="isList">
          <el-select v-model="form.isList" placeholder="请选择" clearable>
            <el-option
              v-for="item in isListOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="路由地址" prop="route">
          <el-input v-model="form.route" placeholder="请输入路由地址"/>
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
import {listSidebar, getSidebar, delSidebar, addSidebar, updateSidebar} from "@/api/business/configure/sidebar";
import {cacheSidebar} from "../../../api/business/cacheRefresh";

export default {
  name: "Sidebar",
  dicts: ['state_type'],
  data() {
    return {
      isListOptions: [
        {
          value: 0,
          label: '无下拉'
        }, {
          value: 1,
          label: '有下拉'
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
      // 侧边栏配置表格数据
      sidebarList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sidebarName: undefined,
        sidebarIcon: undefined,
        order: undefined,
        isList: undefined,
        status: undefined,
        route: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sidebarName: [
          {required: true, message: "侧边栏名称不能为空", trigger: "blur"}
        ],
        order: [
          {required: true, message: "排序不能为空", trigger: "blur"}
        ],
        isList: [
          {required: true, message: "是否有下拉不能为空", trigger: "blur"}
        ],
        route: [
          {required: true, message: "路由地址不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    cacheSidebars() {
      this.buttonLoading = true;
      cacheSidebar().then(res => {
        this.$modal.msgSuccess("刷新成功");
      }).finally(() => this.buttonLoading = false);
    },
    /** 查询侧边栏配置列表 */
    getList() {
      this.loading = true;
      listSidebar(this.queryParams).then(response => {
        this.sidebarList = response.rows;
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
        sidebarName: undefined,
        sidebarIcon: undefined,
        order: undefined,
        isList: undefined,
        status: 0,
        route: undefined
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
      this.title = "添加侧边栏配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getSidebar(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改侧边栏配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateSidebar(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addSidebar(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除侧边栏配置编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delSidebar(ids);
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
      this.download('/business/configure/sidebar/export', {
        ...this.queryParams
      }, `sidebar_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
