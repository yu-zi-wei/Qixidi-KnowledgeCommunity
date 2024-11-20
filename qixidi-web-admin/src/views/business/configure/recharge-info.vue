<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="充值金额" prop="money">
        <el-input
          type="number"
          v-model="queryParams.money"
          placeholder="请输入充值金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="打折活动" prop="isDiscount">
        <el-select v-model="queryParams.isDiscount" placeholder="请选择" clearable>
          <el-option
            v-for="item in discountLis"
            :key="item.key"
            :label="item.value"
            :value="item.key">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="会员折扣" prop="memberDiscount">
        <el-input
          type="number"
          v-model="queryParams.memberDiscount"
          placeholder="请输入会员折扣"
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
          v-hasPermi="['system:rechargeInfo:add']"
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
          v-hasPermi="['system:rechargeInfo:remove']"
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
          v-hasPermi="['system:rechargeInfo:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="rechargeInfoList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="充值金额" align="center" prop="money"/>
      <el-table-column label="获得币的数量" align="center" prop="currency"/>
      <el-table-column label="打折活动" align="center" prop="isDiscount">
        <template v-slot="scope">
          <el-tag :type="scope.row.isDiscount==0? '':'warning'">
            {{ scope.row.isDiscount == 0 ? '未开启' : '已开启' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="具体折扣" align="center" prop="discount"/>
      <el-table-column label="打折开启时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}:{h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="打折结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}:{h}:{i}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="会员折扣" align="center" prop="memberDiscount"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:rechargeInfo:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:rechargeInfo:remove']"
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

    <!-- 添加或修改充值信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-form-item label="充值金额" prop="money">
          <el-input type="number" v-model="form.money" placeholder="请输入充值金额"/>
        </el-form-item>
        <el-form-item label="获得币的数量" prop="currency">
          <el-input type="number" v-model="form.currency" placeholder="请输入获得币的数量"/>
        </el-form-item>
        <el-form-item label="是否开启打折活动" prop="isDiscount">
          <el-radio v-model="form.isDiscount" :label=0>不开启</el-radio>
          <el-radio v-model="form.isDiscount" :label=1>开启</el-radio>
        </el-form-item>
        <el-form-item label="具体折扣" prop="discount" v-show="form.isDiscount==1">
          <el-input type="number" v-model="form.discount" placeholder="请输入具体折扣"/>
        </el-form-item>
        <el-form-item label="打折开始时间" prop="startTime" v-show="form.isDiscount==1">
          <el-date-picker clearable
                          v-model="form.startTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择打折活动开启时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="打折结束时间" prop="endTime" v-show="form.isDiscount==1">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="datetime"
                          value-format="yyyy-MM-dd HH:mm:ss"
                          placeholder="请选择活动结束时间">
          </el-date-picker>
        </el-form-item>
        <el-tooltip content="0：表示不打折，其他数：表示具体折扣" placement="bottom" effect="light">
          <el-form-item label="会员折扣" prop="memberDiscount">
            <el-input type="number" v-model="form.memberDiscount" placeholder="请输入会员折扣"/>
          </el-form-item>
        </el-tooltip>
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
  listRechargeInfo,
  getRechargeInfo,
  delRechargeInfo,
  addRechargeInfo,
  updateRechargeInfo
} from "@/api/business/configure/rechargeInfo";

export default {
  name: "rechargeInfo",
  data() {
    return {
      discountLis: [
        {key: 0, value: "未开启"},
        {key: 1, value: "已开启"}
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
      // 充值信息表格数据
      rechargeInfoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        money: undefined,
        currency: undefined,
        isDiscount: undefined,
        discount: undefined,
        startTime: undefined,
        endTime: undefined,
        memberDiscount: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        id: [
          {required: true, message: "id不能为空", trigger: "blur"}
        ],
        money: [
          {required: true, message: "充值金额不能为空", trigger: "blur"}
        ],
        currency: [
          {required: true, message: "获得币的数量不能为空", trigger: "blur"}
        ],
        isDiscount: [
          {required: true, message: "是否开启打折活动不能为空", trigger: "blur"}
        ],
        memberDiscount: [
          {required: true, message: "会员折扣不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询充值信息列表 */
    getList() {
      this.loading = true;
      listRechargeInfo(this.queryParams).then(response => {
        this.rechargeInfoList = response.rows;
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
        money: undefined,
        currency: undefined,
        isDiscount: undefined,
        discount: undefined,
        startTime: undefined,
        endTime: undefined,
        memberDiscount: undefined
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
      this.title = "添加充值信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getRechargeInfo(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改充值信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateRechargeInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addRechargeInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除充值信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delRechargeInfo(ids);
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
      this.download('business/rechargeInfo/export', {
        ...this.queryParams
      }, `rechargeInfo_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
