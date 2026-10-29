<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="uid" prop="uid">
        <el-input
          v-model="queryParams.uid"
          placeholder="请输入uid"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分组名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入分组名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分组状态" prop="groupState">
        <el-input
          v-model="queryParams.groupState"
          placeholder="请输入分组状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="state">
        <el-input
          v-model="queryParams.state"
          placeholder="请输入状态"
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['business:dictum:album:remove']"
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
          v-hasPermi="['business:dictum:album:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="albumList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="uid" align="center" prop="uid"/>
      <el-table-column label="分组名称" align="center" prop="name"/>
      <el-table-column label="封面" align="center" prop="cover">
        <template slot-scope="scope">
          <el-image fit="cover" :src="scope.row.cover"></el-image>
        </template>
      </el-table-column>
      <el-table-column label="简介" align="center" prop="briefIntroduction"/>
      <el-table-column label="分组状态" align="center" prop="groupState"/>
      <el-table-column label="点赞总数" align="center" prop="helpSum"/>
      <el-table-column label="关注总数" align="center" prop="followSum"/>
      <el-table-column label="专辑状态" align="center" prop="albumState"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['business:dictum:album:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:dictum:album:remove']"
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

    <!-- 添加或修改名言专辑对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="uid" prop="uid">
          <el-input v-model="form.uid" placeholder="请输入uid"/>
        </el-form-item>
        <el-form-item label="分组名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分组名称"/>
        </el-form-item>
        <el-form-item label="封面" prop="cover">
          <el-input v-model="form.cover" placeholder="请输入封面"/>
        </el-form-item>
        <el-form-item label="简介" prop="briefIntroduction">
          <el-input v-model="form.briefIntroduction" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="分组状态" prop="groupState">
          <el-input v-model="form.groupState" placeholder="请输入分组状态"/>
        </el-form-item>
        <el-form-item label="点赞总数" prop="helpSum">
          <el-input v-model="form.helpSum" placeholder="请输入点赞总数"/>
        </el-form-item>
        <el-form-item label="关注总数" prop="followSum">
          <el-input v-model="form.followSum" placeholder="请输入关注总数"/>
        </el-form-item>
        <el-form-item label="状态" prop="state">
          <el-input v-model="form.state" placeholder="请输入状态"/>
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
import {listAlbum, getAlbum, delAlbum, addAlbum, updateAlbum} from "@/api/business/dictumAlbum";

export default {
  name: "Album",
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
      // 名言专辑表格数据
      albumList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uid: undefined,
        name: undefined,
        cover: undefined,
        briefIntroduction: undefined,
        groupState: undefined,
        helpSum: undefined,
        followSum: undefined,
        state: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          {required: true, message: "id不能为空", trigger: "blur"}
        ],
        uid: [
          {required: true, message: "uid不能为空", trigger: "blur"}
        ],
        name: [
          {required: true, message: "分组名称不能为空", trigger: "blur"}
        ],
        cover: [
          {required: true, message: "封面不能为空", trigger: "blur"}
        ],
        briefIntroduction: [
          {required: true, message: "简介不能为空", trigger: "blur"}
        ],
        groupState: [
          {required: true, message: "分组状态不能为空", trigger: "blur"}
        ],
        helpSum: [
          {required: true, message: "点赞总数不能为空", trigger: "blur"}
        ],
        followSum: [
          {required: true, message: "关注总数不能为空", trigger: "blur"}
        ],
        state: [
          {required: true, message: "状态不能为空", trigger: "blur"}
        ],
        createTime: [
          {required: true, message: "创建时间不能为空", trigger: "blur"}
        ],
        updateTime: [
          {required: true, message: "更新时间不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询名言专辑列表 */
    getList() {
      this.loading = true;
      listAlbum(this.queryParams).then(response => {
        this.albumList = response.rows;
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
        uid: undefined,
        name: undefined,
        cover: undefined,
        briefIntroduction: undefined,
        groupState: undefined,
        helpSum: undefined,
        followSum: undefined,
        state: undefined,
        createTime: undefined,
        updateTime: undefined
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
      this.title = "添加名言专辑";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getAlbum(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改名言专辑";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateAlbum(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addAlbum(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除名言专辑编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delAlbum(ids);
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
      this.download('business/dictum/album/export', {
        ...this.queryParams
      }, `album_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
