<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="文章id" prop="articleId">
        <el-input
          v-model="queryParams.articleId"
          placeholder="请输入文章id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文章用户id" prop="uid">
        <el-input
          v-model="queryParams.uid"
          placeholder="请输入文章用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="父级评论id" prop="parentId">
        <el-input
          v-model="queryParams.parentId"
          placeholder="请输入父级评论id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否为顶级评论" prop="isTop">
        <el-input
          v-model="queryParams.isTop"
          placeholder="请输入是否为顶级评论"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论人id" prop="commentUid">
        <el-input
          v-model="queryParams.commentUid"
          placeholder="请输入评论人id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论状态" prop="state">
        <el-input
          v-model="queryParams.state"
          placeholder="请输入评论状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论时间" prop="deleteTime">
        <el-date-picker clearable
                        v-model="queryParams.createTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="评论时间">
        </el-date-picker>
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
          v-hasPermi="['business:comment:remove']"
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
          v-hasPermi="['business:comment:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="commentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="文章id" align="center" prop="articleId"/>
      <el-table-column label="文章用户id" align="center" prop="uid"/>
      <el-table-column label="父级评论id" align="center" prop="parentId"/>
      <el-table-column label="是否为顶级评论" align="center" prop="isTop"/>
      <el-table-column label="目标id" align="center" prop="targetId"/>
      <el-table-column label="目标用户id" align="center" prop="targetUid"/>
      <el-table-column label="评论人id" align="center" prop="commentUid"/>
      <el-table-column label="评论内容" align="center" prop="content"/>
      <el-table-column label="评论类型" align="center" prop="type"/>
      <el-table-column label="评论状态" align="center" prop="state"/>
      <el-table-column label="更新时间" align="center" prop="updateTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}:{h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['business:comment:remove']"
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

    <!-- 添加或修改文章评论对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文章id" prop="articleId">
          <el-input v-model="form.articleId" placeholder="请输入文章id"/>
        </el-form-item>
        <el-form-item label="文章用户id" prop="uid">
          <el-input v-model="form.uid" placeholder="请输入文章用户id"/>
        </el-form-item>
        <el-form-item label="父级评论id" prop="parentId">
          <el-input v-model="form.parentId" placeholder="请输入父级评论id"/>
        </el-form-item>
        <el-form-item label="是否为顶级评论" prop="isTop">
          <el-input v-model="form.isTop" placeholder="请输入是否为顶级评论"/>
        </el-form-item>
        <el-form-item label="评论人id" prop="commentUid">
          <el-input v-model="form.commentUid" placeholder="请输入评论人id"/>
        </el-form-item>
        <el-form-item label="评论内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="评论状态" prop="state">
          <el-input v-model="form.state" placeholder="请输入评论状态"/>
        </el-form-item>
        <el-form-item label="更新时间" prop="updateTime">
          <el-date-picker clearable
                          v-model="form.deleteTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择更新时间">
          </el-date-picker>
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
import {listComment, getComment, delComment, addComment, updateComment} from "@/api/business/comment/articleComment";

export default {
  name: "articleComment",
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
      // 文章评论表格数据
      commentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        articleId: undefined,
        uid: undefined,
        parentId: undefined,
        isTop: undefined,
        targetId: undefined,
        targetUid: undefined,
        commentUid: undefined,
        content: undefined,
        type: undefined,
        state: undefined,
        updateTime: undefined,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          {required: true, message: "id不能为空", trigger: "blur"}
        ],
        articleId: [
          {required: true, message: "文章id不能为空", trigger: "blur"}
        ],
        uid: [
          {required: true, message: "文章用户id不能为空", trigger: "blur"}
        ],
        parentId: [
          {required: true, message: "父级评论id不能为空", trigger: "blur"}
        ],
        isTop: [
          {required: true, message: "是否为顶级评论不能为空", trigger: "blur"}
        ],
        targetId: [
          {required: true, message: "目标id不能为空", trigger: "blur"}
        ],
        targetUid: [
          {required: true, message: "目标用户id不能为空", trigger: "blur"}
        ],
        commentUid: [
          {required: true, message: "评论人id不能为空", trigger: "blur"}
        ],
        content: [
          {required: true, message: "评论内容不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "评论类型不能为空", trigger: "change"}
        ],
        state: [
          {required: true, message: "评论状态不能为空", trigger: "blur"}
        ],
        createTime: [
          {required: true, message: "创建时间不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询文章评论列表 */
    getList() {
      this.loading = true;
      listComment(this.queryParams).then(response => {
        this.commentList = response.rows;
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
        articleId: undefined,
        uid: undefined,
        parentId: undefined,
        isTop: undefined,
        targetId: undefined,
        targetUid: undefined,
        commentUid: undefined,
        content: undefined,
        type: undefined,
        state: undefined,
        createTime: undefined,
        deleteTime: undefined
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
      this.title = "添加文章评论";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getComment(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改文章评论";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateComment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addComment(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除文章评论编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delComment(ids);
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
      this.download('business/article/comment/export', {
        ...this.queryParams
      }, `comment_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
