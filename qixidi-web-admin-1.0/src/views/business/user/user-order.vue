<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户id" prop="uid">
        <el-input
          v-model="queryParams.uid"
          placeholder="请输入用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="充值信息id" prop="rechargeInfoId">
        <el-input
          v-model="queryParams.rechargeInfoId"
          placeholder="请输入充值信息id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="折扣" prop="discount">
        <el-input
          v-model="queryParams.discount"
          placeholder="请输入折扣"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付方式" prop="paymentMethod">
        <el-input
          v-model="queryParams.paymentMethod"
          placeholder="请输入支付方式"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付状态" prop="paymentState">
        <el-input
          v-model="queryParams.paymentState"
          placeholder="请输入支付状态"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="支付时间" prop="paymentTime">
        <el-date-picker clearable
                        v-model="queryParams.paymentTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择支付时间">
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
          v-hasPermi="['system:order:remove']"
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
          v-hasPermi="['system:order:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="orderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id" v-if="true"/>
      <el-table-column label="用户id" align="center" prop="uid"/>
      <el-table-column label="充值信息id" align="center" prop="rechargeInfoId"/>
      <el-table-column label="充值金额" align="center" prop="rechargeMoney"/>
      <el-table-column label="获得A币数量" align="center" prop="rechargeCurrency"/>
      <el-table-column label="折扣" align="center" prop="discount"/>
      <el-table-column label="支付方式" align="center" prop="paymentMethod"/>
      <el-table-column label="支付状态" align="center" prop="paymentState">
        <template slot-scope="scope">
          <el-tag v-show="scope.row.paymentState==1" size="small" type="warning">待支付</el-tag>
          <el-tag v-show="scope.row.paymentState==2" size="small" type="success">支付完成</el-tag>
          <el-tag v-show="scope.row.paymentState==3" size="small" type="info">已过期</el-tag>
          <el-tag v-show="scope.row.paymentState==4" size="small" type="danger">支付失败</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="支付时间" align="center" prop="paymentTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.paymentTime, '{y}-{m}-{d}:{h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:order:remove']"
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

    <!-- 添加或修改用户订单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户id" prop="uid">
          <el-input v-model="form.uid" placeholder="请输入用户id"/>
        </el-form-item>
        <el-form-item label="充值信息id" prop="rechargeInfoId">
          <el-input v-model="form.rechargeInfoId" placeholder="请输入充值信息id"/>
        </el-form-item>
        <el-form-item label="充值金额" prop="rechargeMoney">
          <el-input v-model="form.rechargeMoney" placeholder="请输入充值金额"/>
        </el-form-item>
        <el-form-item label="获得A币数量" prop="rechargeCurrency">
          <el-input v-model="form.rechargeCurrency" placeholder="请输入获得A币数量"/>
        </el-form-item>
        <el-form-item label="折扣" prop="discount">
          <el-input v-model="form.discount" placeholder="请输入折扣"/>
        </el-form-item>
        <el-form-item label="支付方式" prop="paymentMethod">
          <el-input v-model="form.paymentMethod" placeholder="请输入支付方式"/>
        </el-form-item>
        <el-form-item label="支付状态" prop="paymentState">
          <el-input v-model="form.paymentState" placeholder="请输入支付状态"/>
        </el-form-item>
        <el-form-item label="支付时间" prop="paymentTime">
          <el-date-picker clearable
                          v-model="form.paymentTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择支付时间">
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
import {listOrder, getOrder, delOrder, addOrder, updateOrder} from "@/api/business/userOrder";

export default {
  name: "userOrder",
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
      // 用户订单表格数据
      orderList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        uid: undefined,
        rechargeInfoId: undefined,
        rechargeMoney: undefined,
        rechargeCurrency: undefined,
        discount: undefined,
        paymentMethod: undefined,
        paymentState: undefined,
        paymentTime: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          {required: true, message: "id不能为空", trigger: "blur"}
        ],
        uid: [
          {required: true, message: "用户id不能为空", trigger: "blur"}
        ],
        rechargeInfoId: [
          {required: true, message: "充值信息id不能为空", trigger: "blur"}
        ],
        rechargeMoney: [
          {required: true, message: "充值金额不能为空", trigger: "blur"}
        ],
        rechargeCurrency: [
          {required: true, message: "获得A币数量不能为空", trigger: "blur"}
        ],
        discount: [
          {required: true, message: "折扣不能为空", trigger: "blur"}
        ],
        paymentMethod: [
          {required: true, message: "支付方式不能为空", trigger: "blur"}
        ],
        createTime: [
          {required: true, message: "订单创建时间不能为空", trigger: "blur"}
        ],
        paymentState: [
          {required: true, message: "支付状态不能为空", trigger: "blur"}
        ],
        paymentTime: [
          {required: true, message: "支付时间不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户订单列表 */
    getList() {
      this.loading = true;
      listOrder(this.queryParams).then(response => {
        this.orderList = response.rows;
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
        rechargeInfoId: undefined,
        rechargeMoney: undefined,
        rechargeCurrency: undefined,
        discount: undefined,
        paymentMethod: undefined,
        createTime: undefined,
        paymentState: undefined,
        paymentTime: undefined
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
      this.title = "添加用户订单";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getOrder(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改用户订单";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateOrder(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addOrder(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除用户订单编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delOrder(ids);
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
      this.download('business/order/export', {
        ...this.queryParams
      }, `order_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
