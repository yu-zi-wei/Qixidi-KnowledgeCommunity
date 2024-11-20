<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="作者名称" prop="nickname">
        <el-input
          v-model="queryParams.nickname"
          placeholder="请输入作者名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文章标题" prop="articleTitle">
        <el-input
          v-model="queryParams.articleTitle"
          placeholder="请输入文章标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文章分类" prop="groupingId">
        <el-select v-model="queryParams.groupingId" filterable placeholder="请选择分类">
          <el-option
            v-for="item in listGroupingInfoArr"
            :key="item.id"
            :label="item.groupingName"
            :value="item.id"
            @keyup.enter.native="handleQuery">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型">
          <el-option
            v-for="item in articleTypeList"
            :key="item.code"
            :label="item.value"
            :value="item.code"
            @keyup.enter.native="handleQuery">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标签名称" prop="labelId">
        <el-select v-model="queryParams.labelId" filterable placeholder="请选择标签">
          <el-option
            v-for="item in listInfoArr"
            :key="item.id"
            :label="item.labelName"
            :value="item.id"
            @keyup.enter.native="handleQuery">
          </el-option>
        </el-select>

        <el-form-item label="状态" prop="auditState">
          <el-select v-model="queryParams.auditState" placeholder="请选择状态">
            <el-option
              v-for="item in auditStatusList"
              :key="item.code"
              :label="item.value"
              :value="item.code"
              @keyup.enter.native="handleQuery">
            </el-option>
          </el-select>
        </el-form-item>

      </el-form-item>
      <!--      <el-form-item label="审核时间" prop="auditTime">-->
      <!--        <el-date-picker clearable-->
      <!--                        v-model="queryParams.auditTime"-->
      <!--                        type="date"-->
      <!--                        value-format="yyyy-MM-dd"-->
      <!--                        placeholder="请选择审核时间">-->
      <!--        </el-date-picker>-->
      <!--      </el-form-item>-->
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['article:information:remove']"
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
          v-hasPermi="['article:information:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="informationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <!--      <el-table-column label="文章id" align="center" prop="id" v-if="true"/>-->
      <el-table-column label="作者名称" align="center" prop="nickname"/>
      <el-table-column label="头像" align="center" prop="avatar">
        <template slot-scope="scope">
          <el-avatar :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column label="文章标题" align="center" prop="articleTitle"/>
      <el-table-column label="文章封面" align="center" prop="articleCover">
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.articleCover"
          ></el-image>
        </template>
      </el-table-column>
      <el-table-column label="文章类型" align="center" prop="type">
        <template slot-scope="scope">
          <el-tag :type="scope.row.type == 1?'':'warning'">
            {{ scope.row.type == 1 ? '原创' : '转载' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="文章状态" align="center" prop="state">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.state == 0">正常</el-tag>
          <el-tag type="danger" v-if="scope.row.state == 1">已删除</el-tag>
          <el-tag type="warning" v-if="scope.row.state == 2">草稿</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="文章分类" align="center" prop="groupingName"/>
      <el-table-column label="标签名称" align="center" prop="labelName">
        <template slot-scope="scope">
          <div v-if="scope.row.labelNameList.length>0">
            <el-tag v-for="items of scope.row.labelNameList" style="margin-bottom: 6px">
              {{ items }}
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="是否公开" align="center" prop="isPublic">
        <template slot-scope="scope">
          <el-tag :type="scope.row.isPublic == 1?'success':'warning'">
            {{ scope.row.isPublic == 1 ? '公开' : '不公开' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" align="center" prop="auditState">
        <template slot-scope="scope">
          <el-tag type="info" v-if="scope.row.auditState==1">审核中</el-tag>
          <el-tag type="success" v-if="scope.row.auditState==2">审核通过</el-tag>
          <el-tag type="danger" v-if="scope.row.auditState==3">未通过</el-tag>
          <el-tag type="warning" v-if="scope.row.auditState==4">草稿</el-tag>
        </template>
      </el-table-column>
      <!--      <el-table-column label="审核时间" align="center" prop="auditTime" width="180">-->
      <!--        <template slot-scope="scope">-->
      <!--          <span>{{ parseTime(scope.row.auditTime, '{y}-{m}-{d}:{h}:{i}') }}</span>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}:{h}:{i}') }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['article:information:remove']"
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
  </div>
</template>

<script>
import {
  listInformation,
  getInformation,
  delInformation,
  addInformation,
  updateInformation
} from "@/api/business/article/articleInform";
import {listGroupingInfo} from "@/api/business/label/groupingInfo";
import {listInfo} from "@/api/business/label/info";

export default {
  name: "Information",
  data() {
    return {
      listGroupingInfoArr: [],
      listInfoArr: [],
      articleTypeList: [
        {code: 1, value: "原创"},
        {code: 2, value: "转载"},
      ],
      auditStatusList: [
        {code: 1, value: "审核中", type: "info"},
        {code: 2, value: "审核通过", type: "success"},
        {code: 3, value: "未通过", type: "danger"},
        {code: 4, value: "草稿", type: "warning"},
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
      // 文章信息表格数据
      informationList: [],
      daterangeCreateTime: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: undefined,
        articleTitle: undefined,
        articleCover: undefined,
        articleAbstract: undefined,
        articleContent: undefined,
        type: undefined,
        reprintUrl: undefined,
        status: undefined,
        updateId: undefined,
        specialId: undefined,
        groupingId: undefined,
        labelId: undefined,
        labelName: undefined,
        specialName: undefined,
        likeTimes: undefined,
        numberTimes: undefined,
        isPublic: undefined,
        auditStatus: undefined,
        auditTime: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
    };
  },
  created() {
    this.getList();
    this.listGroupingInfoArrs();
  },
  methods: {
    listGroupingInfoArrs() {
      listGroupingInfo().then(res => {
        this.listGroupingInfoArr = res.rows;
      });
      listInfo().then(res => {
        this.listInfoArr = res.rows;
      });
    },
    /** 查询文章信息列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && '' != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] = this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      listInformation(this.queryParams).then(response => {
        this.informationList = response.rows;
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
        userId: undefined,
        articleTitle: undefined,
        articleCover: undefined,
        articleAbstract: undefined,
        articleContent: undefined,
        type: undefined,
        reprintUrl: undefined,
        status: 0,
        createTime: undefined,
        updateId: undefined,
        updateTime: undefined,
        specialId: undefined,
        groupingId: undefined,
        labelId: undefined,
        labelName: undefined,
        specialName: undefined,
        likeTimes: undefined,
        numberTimes: undefined,
        isPublic: undefined,
        auditState: 0,
        auditTime: undefined
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
      this.daterangeCreateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.loading = true;
      this.reset();
      const id = row.id || this.ids
      getInformation(id).then(response => {
        this.loading = false;
        this.form = response.data;
        this.open = true;
        this.title = "修改文章信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.buttonLoading = true;
          if (this.form.id != null) {
            updateInformation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).finally(() => {
              this.buttonLoading = false;
            });
          } else {
            addInformation(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除文章信息编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return delInformation(ids);
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
      this.download('business/article/information/export', {
        ...this.queryParams
      }, `文章列表${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

