<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="父级菜单" prop="parentId">
        <el-select v-model="queryParams.parentId" filterable placeholder="请选择">
          <el-option
            v-for="item in toolParentList"
            :key="item.id"
            :label="item.toolName"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="工具名称" prop="toolName">
        <el-input
          v-model="queryParams.toolName"
          placeholder="请输入工具名称"
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
          v-hasPermi="['business:toolInfo:add']"
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
          v-hasPermi="['business:toolInfo:remove']"
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
          v-hasPermi="['business:toolInfo:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-view"
          size="mini"
          @click="drawer = true"
        >查看父级数据
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-table v-loading="loading" :data="toolInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="父级id" align="center" prop="parentId"/>
      <el-table-column label="工具名称" align="center" prop="toolName"/>
      <el-table-column label="图标" align="center" prop="icon">
        <template v-slot="scope">
          <el-image
            style="width: 60px; height: 60px"
            :src="scope.row.icon"
            fit="contain"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="工具地址" align="center" prop="toolUrl">
        <template v-slot="scope">
          <a :href="scope.row.toolUrl" target="_blank" class="cursor-pointer">{{ scope.row.toolUrl }}</a>
        </template>
      </el-table-column>
<!--      <el-table-column label="类型" align="center" prop="type"/>-->
      <el-table-column label="状态" align="center" prop="state">
        <template v-slot="scope">
          <dict-tag :options="dict.type.state_type" :value="scope.row.state"/>
        </template>
      </el-table-column>
      <el-table-column label="创建人" align="center" prop="createBy"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:toolInfo:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:toolInfo:remove']"
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

    <!-- 添加或修改工具信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="父级菜单" prop="parentId">
          <el-select v-model="form.parentId" filterable placeholder="请选择">
            <el-option
              v-for="item in toolParentList"
              :key="item.id"
              :label="item.toolName"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="工具名称" prop="toolName">
          <el-input v-model="form.toolName" placeholder="请输入工具名称"/>
        </el-form-item>
        <el-form-item label="工具描述" prop="describe">
          <el-input v-model="form.describe" placeholder="请输入工具描述"/>
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标"/>
        </el-form-item>
        <el-form-item label="工具地址" prop="toolUrl">
          <el-input v-model="form.toolUrl" placeholder="请输入工具地址"/>
        </el-form-item>
        <el-form-item label="排序" prop="order">
          <el-input-number v-model="form.order" :min="1" :max="99" label="请输入排序"></el-input-number>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <el-drawer
      title="父级工具列表"
      size="60%"
      :visible.sync="drawer"
      :with-header="false">
      <div style="margin: 10px 0 10px 20px">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAddParent"
          v-hasPermi="['business:toolInfo:add']"
        >新增父级工具菜单
        </el-button>
      </div>
      <el-table v-loading="loading" :data="toolParentList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="id" align="center" prop="id"/>
        <el-table-column label="工具名称" align="center" prop="toolName"/>
        <el-table-column label="图标" align="center" prop="icon"/>
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
              v-hasPermi="['business:toolInfo:edit']"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['business:toolInfo:remove']"
            >删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :title="titleParent" :visible.sync="openParent" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="工具名称" prop="toolName">
            <el-input v-model="form.toolName" placeholder="请输入工具名称"/>
          </el-form-item>
          <el-form-item label="图标" prop="icon">
            <el-input v-model="form.icon" placeholder="请输入图标"/>
          </el-form-item>
          <el-form-item label="工具地址" prop="toolUrl">
            <el-input v-model="form.toolUrl" placeholder="请输入工具地址"/>
          </el-form-item>
          <el-form-item label="排序" prop="order">
            <el-input-number v-model="form.order" :min="1" :max="99" label="请输入排序"></el-input-number>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </el-drawer>

  </div>
</template>

<script>
import {listToolInfo, getToolInfo, delToolInfo, addToolInfo, updateToolInfo} from "@/api/business/configure/toolInfo";

export default {
  name: "ToolInfo",
  dicts: ['state_type'],
  data() {
    return {
      drawer: false,
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
      // 工具信息表格数据
      toolInfoList: [],
      //父级数据
      toolParentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        parentId: undefined,
        toolName: undefined,
        icon: undefined,
        isParent: 2,
        toolUrl: undefined,
        order: undefined,
        type: undefined,
        state: undefined,
        uid: undefined
      },
      // 表单参数
      form: {
        isParent: 2,
      },
      // 表单校验
      rules: {
        toolName: [
          {required: true, message: "工具名称不能为空", trigger: "blur"}
        ],
        toolUrl: [
          {required: true, message: "工具地址不能为空", trigger: "blur"}
        ],
        order: [
          {required: true, message: "排序不能为空", trigger: "blur"}
        ],
      },
      openParent: false,
      titleParent: "添加父级工具信息",
    };
  },
  created() {
    this.getList();
  },
  methods: {
    handleAddParent() {
      this.reset();
      this.openParent = true;
      this.form.isParent = 1;
      this.titleParent = "添加父级工具信息";
    },
    /** 查询工具信息列表 */
    getList() {
      this.loading = true;
      listToolInfo(this.queryParams).then(response => {
        this.toolInfoList = response.data.parentFalse.records;
        this.toolParentList = response.data.parentTrue;
        this.total = response.data.parentFalse.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.openParent = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        parentId: undefined,
        toolName: undefined,
        icon: undefined,
        isParent: undefined,
        toolUrl: undefined,
        order: undefined,
        type: undefined,
        state: undefined,
        createTime: undefined,
        uid: undefined
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
      this.form.isParent = 2;
      this.title = "添加工具信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getToolInfo(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改工具信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateToolInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.openParent = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addToolInfo(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.openParent = false;
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
      this.$modal.confirm('是否确认删除工具信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delToolInfo(ids);
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
      this.download('business/toolInfo/export', {
        ...this.queryParams
      }, `toolInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
