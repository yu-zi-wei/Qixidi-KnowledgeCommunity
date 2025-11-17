<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户名" prop="username">
        <el-input
          v-model="queryParams.username"
          placeholder="请输入用户名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户昵称" prop="nickname">
        <el-input
          v-model="queryParams.nickname"
          placeholder="请输入用户昵称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="用户来源" prop="source">
        <el-input
          v-model="queryParams.source"
          placeholder="请输入用户来源"
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

    <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="用户头像" align="center" prop="avatar">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column label="用户名" align="center" prop="username"/>
      <el-table-column label="用户昵称" align="center" prop="nickname"/>
      <el-table-column label="用户角色" align="center" prop="roleId">
        <template slot-scope="scope">
          {{ scope.row.roleId === 1 ? '普通用户' : (scope.row.roleId === 2 ? '创作者' : '管理员') }}
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center" prop="email"/>
      <el-table-column label="职业" align="center" prop="occupation"/>
      <el-table-column label="用户备注" align="center" prop="remark"/>
      <el-table-column label="用户来源" align="center" prop="source"/>
      <el-table-column label="注册时间" align="center" prop="createTime">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}:{h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="text" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
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

    <!-- 添加或修改【用户】对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名">
          <el-input disabled v-model="form.username" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input disabled v-model="form.nickname" placeholder="请输入用户昵称"/>
        </el-form-item>
        <el-form-item label="用户角色" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            >
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
import { addUser, delUser, getUser, listUser, updateUser } from '@/api/system/userMain'

export default {
  name: 'User',
  data() {
    return {
      options: [{
        value: 1,
        label: '普通用户'
      }, {
        value: 2,
        label: '创作者'
      }, {
        value: 3,
        label: '管理员'
      }],
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
      // 【用户】表格数据
      userList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        username: undefined,
        nickname: undefined,
        blog: undefined,
        company: undefined,
        location: undefined,
        email: undefined,
        gender: undefined,
        source: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roleId: [
          { required: true, message: '用户角色不能为空', trigger: 'blur' }
        ]

      },
      list: []
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询【用户】列表 */
    getList() {
      this.loading = true
      listUser(this.queryParams).then(response => {
        this.userList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        uuid: undefined,
        username: undefined,
        nickname: undefined,
        avatar: undefined,
        blog: undefined,
        company: undefined,
        location: undefined,
        email: undefined,
        gender: undefined,
        remark: undefined,
        source: undefined
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.uuid)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加【用户】'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true
      this.reset()
      const uuid = row.uuid || this.ids
      getUser(uuid).then(response => {
        this.loading = false
        this.form = response.data
        this.open = true
        this.title = '修改【用户】'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          this.buttonLoading = true
          if (this.form.uuid != null) {
            updateUser({
              uuid: this.form.uuid,
              roleId: this.form.roleId
            }).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          } else {
            addUser(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            }).finally(() => {
              this.buttonLoading = false
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const uuids = row.uuid || this.ids
      this.$modal.confirm('是否确认删除【用户】编号为"' + uuids + '"的数据项？').then(() => {
        this.loading = true
        return delUser(uuids)
      }).then(() => {
        this.loading = false
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).finally(() => {
        this.loading = false
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/user/export', {
        ...this.queryParams
      }, `user_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
