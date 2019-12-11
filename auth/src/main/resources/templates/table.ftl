<template>
    <div class="main-area">
        <el-breadcrumb separator=":">
            <el-breadcrumb-item>当前位置</el-breadcrumb-item>
            <el-breadcrumb-item>诉讼类案件</el-breadcrumb-item>
        </el-breadcrumb>
        <!--查询区域-->
        <el-row class="margin-top-20">
            <el-col :span="3">
                <label class="search-label">
                    纠纷发生法人主体:
                </label>
            </el-col>
            <el-col :span="4">
                <el-input v-model="searchForm.ascriptionCompany"
                          placeholder="纠纷发生法人主体"
                          maxlength="32"
                          :size="GLOBAL.config.systemSize"></el-input>
            </el-col>
            <el-col :span="2">
                <label class="search-label">
                    服务人员:
                </label>
            </el-col>
            <el-col :span="3">
                <el-select v-model="searchForm.servicePersonal"
                           placeholder="服务人员"
                           :size="GLOBAL.config.systemSize"
                           style="width: 100%;"
                           filterable
                           :filter-method="serviceSearch"
                >
                    <el-option label="--请选择--" value=""></el-option>
                    <el-option :label="item.name +'(' + item.domainName + ')'" :value="item.id"
                               v-for="item in dictionary.psPersonnelList" :key="item.id"></el-option>
                </el-select>
            </el-col>
            <el-col :span="2">
                <label class="search-label">
                    案件类型:
                </label>
            </el-col>
            <el-col :span="3">
                <el-select v-model="searchForm.caseType"
                           placeholder="案件类型"
                           :size="GLOBAL.config.systemSize"
                           style="width: 100%;"
                >
                    <el-option label="--请选择--" value=""></el-option>
                    <el-option :label="item.name" :value="item.code"
                               v-for="item in dictionary.caseType" :key="item.id"></el-option>
                </el-select>
            </el-col>
            <el-col :span="2">
                <label class="search-label">
                    纠纷类型:
                </label>
            </el-col>
            <el-col :span="3">
                <el-select v-model="searchForm.disputeType"
                           placeholder="纠纷类型"
                           :size="GLOBAL.config.systemSize"
                           style="width: 100%;"
                >
                    <el-option label="--请选择--" value=""></el-option>
                    <el-option :label="item.name" :value="item.code"
                               v-for="item in dictionary.disputeType" :key="item.id"></el-option>
                </el-select>
            </el-col>
            <el-col :span="2">
                <el-button type="primary" @click="doSearch" :size="GLOBAL.config.systemSize" icon="el-icon-search">查询
                </el-button>
            </el-col>
        </el-row>
        <el-row class="margin-top-10">
            <el-col :span="3">
                <label class="search-label">
                    对方名称:
                </label>
            </el-col>
            <el-col :span="4">
                <el-input
                        v-model="searchForm.targetName"
                        placeholder="对方名称"
                        maxlength="16"
                        :size="GLOBAL.config.systemSize"></el-input>
            </el-col>
            <el-col :span="2">
                <label class="search-label">
                    金额范围:
                </label>
            </el-col>
            <el-col :span="6">
                <el-input-number style="width: 40%;"
                                 v-model="searchForm.moneyStart"
                                 controls-position="right"
                                 :min="0"
                                 :precision="2"
                                 :step="0.1"
                                 :size="GLOBAL.config.systemSize" placeholder="最低金额"></el-input-number>
                <span class="division-style">~</span>
                <el-input-number style="width: 40%;" v-model="searchForm.moneyEnd" controls-position="right" :precision="2"
                                 :step="0.1"
                                 :size="GLOBAL.config.systemSize"
                                 :min="searchForm.moneyStart"
                                 placeholder="最高金额"></el-input-number>
            </el-col>
            <el-col :span="2">
                <label class="search-label">
                    申请日期:
                </label>
            </el-col>
            <el-col :span="5">
                <el-date-picker
                        :size="GLOBAL.config.systemSize"
                        v-model="searchForm.applyDate"
                        type="daterange"
                        align="right"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        style="width: 100%;"
                        :picker-options="pickerOptions">
                </el-date-picker>
            </el-col>
            <el-col :span="2">
                <el-button type="primary" @click="exportTableData" :size="GLOBAL.config.systemSize" icon="el-icon-download">导出
                </el-button>
            </el-col>
        </el-row>
        <el-row class="margin-top-20">
            <el-col :span="4">
                <el-button
                        :size="GLOBAL.config.systemSize"
                        @click="deleteBatch"
                        type="danger"
                        icon="el-icon-delete"
                        :disabled="!deletebatchList.deleteFlag"
                >删除
                </el-button>
                <el-button :size="GLOBAL.config.systemSize" @click="addData" type="primary"
                           icon="el-icon-plus">新增
                </el-button>
            </el-col>
        </el-row>
        <el-tabs v-model="tabSelect" @tab-click="tabChange">
            <!--在办-->
            <el-tab-pane label="在办" name="working">
                <el-table style="width: 100%"
                          :data="tableData.working"
                          @selection-change="handleWorkingSelectionChange"
                          element-loading-text="数据处理中...请稍等..."
                          v-loading="loading"
                >
                    <el-table-column
                            type="selection">
                    </el-table-column>
                    <el-table-column prop="applyDate" label="申请日期">
                        <template slot-scope="scope">
                            {{ scope.row.applyDate?$moment(scope.row.applyDate).format(GLOBAL.config.dateFormat.ymd):'' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="ascriptionCompany" label="纠纷发生法人主体" align="center" width="140"/>
                    <el-table-column prop="targetName" label="对方名称" align="center"/>
                    <el-table-column prop="caseType" label="案件类型" align="center">
                        <template slot-scope="scope">
                            {{
                            FUNCTIONS.systemFunction.getConfigValue(
                            scope.row.caseType,
                            GLOBAL.config.dictionaryPre + GLOBAL.config.dictionary.caseType)
                            }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="disputeType" label="纠纷类型" align="center">
                        <template slot-scope="scope">
                            {{
                            FUNCTIONS.systemFunction.getConfigValue(
                            scope.row.disputeType,
                            GLOBAL.config.dictionaryPre + GLOBAL.config.dictionary.disputeType)
                            }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="money" label="金额" align="center"/>
                    <el-table-column prop="servicePersonal" label="服务人员" align="center">
                        <template slot-scope="scope">
                            {{
                            FUNCTIONS.systemFunction.getPsValue(
                            scope.row.servicePersonal,
                            GLOBAL.config.psDataName)
                            }}
                            <i class="el-icon-s-operation" style="cursor: pointer; color: #409EFF;" title="修改服务人员"
                               @click="modifyServicePersonal(scope.row)"></i>
                        </template>
                    </el-table-column>
                    <el-table-column prop="updateTime" label="更新时间" width="140">
                        <template slot-scope="scope">
                            {{ scope.row.updateTime?$moment(scope.row.updateTime).format(GLOBAL.config.dateFormat.ymdhm):'' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="scope" label="操作" align="center">
                        <template slot-scope="scope">
                            <el-dropdown>
                <span class="el-dropdown-link operator-text">
                  选择操作<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item :icon="item.icon" v-for="item in getSource(scope.row)"
                                                      :key="item.method"
                                                      @click.native="handleCommon(item.method, scope.row)">{{item.title}}
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                        class="margin-top-10 margin-bottom-20"
                        @size-change="handleWorkingSizeChange"
                        @current-change="handleWorkingCurrentChange"
                        :current-page="pagination.working.currentPage"
                        :page-sizes="pagination.working.pageSizeList"
                        :page-size="pagination.working.pageSize"
                        :layout="pagination.working.layout"
                        :total="pagination.working.total">
                </el-pagination>
            </el-tab-pane>
            <!--办结-->
            <el-tab-pane label="结案" name="closed">
                <el-table style="width: 100%"
                          :data="tableData.closed"
                          @selection-change="handleClosedSelectionChange"
                >
                    <el-table-column
                            type="selection">
                    </el-table-column>
                    <el-table-column prop="applyDate" label="申请日期">
                        <template slot-scope="scope">
                            {{ scope.row.applyDate?$moment(scope.row.applyDate).format(GLOBAL.config.dateFormat.ymd):'' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="ascriptionCompany" label="纠纷发生法人主体" align="center" width="140"/>
                    <el-table-column prop="targetName" label="对方名称" align="center"/>
                    <el-table-column prop="caseType" label="案件类型" align="center">
                        <template slot-scope="scope">
                            {{
                            FUNCTIONS.systemFunction.getConfigValue(
                            scope.row.caseType,
                            GLOBAL.config.dictionaryPre + GLOBAL.config.dictionary.caseType)
                            }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="disputeType" label="纠纷类型" align="center">
                        <template slot-scope="scope">
                            {{
                            FUNCTIONS.systemFunction.getConfigValue(
                            scope.row.disputeType,
                            GLOBAL.config.dictionaryPre + GLOBAL.config.dictionary.disputeType)
                            }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="money" label="金额" align="center"/>
                    <el-table-column prop="servicePersonal" label="服务人员" align="center">
                        <template slot-scope="scope">
                            {{
                            FUNCTIONS.systemFunction.getPsValue(
                            scope.row.servicePersonal,
                            GLOBAL.config.psDataName)
                            }}
                            <i class="el-icon-s-operation" style="cursor: pointer; color: #409EFF;" title="修改服务人员"
                               @click="modifyServicePersonal(scope.row)"></i>
                        </template>
                    </el-table-column>
                    <el-table-column prop="updateTime" label="更新时间" width="140">
                        <template slot-scope="scope">
                            {{ scope.row.updateTime?$moment(scope.row.updateTime).format(GLOBAL.config.dateFormat.ymdhm):'' }}
                        </template>
                    </el-table-column>
                    <el-table-column prop="scope" label="操作" align="center">
                        <template slot-scope="scope">
                            <el-dropdown>
                <span class="el-dropdown-link operator-text">
                  选择操作<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                                <el-dropdown-menu slot="dropdown">
                                    <el-dropdown-item :icon="item.icon" v-for="item in getSource(scope.row)"
                                                      :key="item.method"
                                                      @click.native="handleCommon(item.method, scope.row)">{{item.title}}
                                    </el-dropdown-item>
                                </el-dropdown-menu>
                            </el-dropdown>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                        class="margin-top-10 margin-bottom-20"
                        @size-change="handleClosedSizeChange"
                        @current-change="handleClosedCurrentChange"
                        :current-page="pagination.closed.currentPage"
                        :page-sizes="pagination.closed.pageSizeList"
                        :page-size="pagination.closed.pageSize"
                        :layout="pagination.closed.layout"
                        :total="pagination.closed.total">
                </el-pagination>
            </el-tab-pane>
        </el-tabs>
        <!--新增-->
        <el-dialog
                :fullscreen="true"
                :show-close="false"
                :visible.sync="addVisibleFlag"
                :destroy-on-close="true"
        >
            <operationAdd
                    ref="operationAdd"
                    :refresh="init"
                    :close-self="()=> addVisibleFlag = false"
            />
        </el-dialog>
        <!--编辑-->
        <el-dialog
                :fullscreen="true"
                :show-close="false"
                :visible.sync="editVisibleFlag"
                :destroy-on-close="true"
        >
            <operationEdit
                    ref="operationEdit"
                    :refresh="init"
                    :close-self="()=> editVisibleFlag = false"
            />
        </el-dialog>
        <!--查看-->
        <el-dialog
                :fullscreen="true"
                :show-close="false"
                :visible.sync="viewVisibleFlag"
                :destroy-on-close="true"
        >
            <operationView
                    ref="operationView"
                    :refresh="init"
                    :close-self="()=> viewVisibleFlag = false"
            />
        </el-dialog>
        <!--提醒-->
        <el-dialog
                width="80%"
                :visible.sync="remindVisibleFlag"
                :destroy-on-close="true"
        >
            <operationRemind
                    ref="operationRemind"
                    :refresh="init"
                    :close-self="()=> remindVisibleFlag = false"
            />
        </el-dialog>
        <!--更换服务人员-->
        <el-dialog
                title="变更服务人员"
                width="300px"
                :visible.sync="changeServiceVisibleFlag"
                :destroy-on-close="true"
        >
            <operationPersonalChange
                    ref="operationPersonalChange"
                    :refresh="init"
                    :close-self="()=> changeServiceVisibleFlag = false"
            />
        </el-dialog>
    </div>
</template>
<script>
    import operationAdd from './OperationAdd'
    import operationEdit from './OperationEdit'
    import operationView from './OperationView'
    import operationRemind from './OperationRemind'
    import operationPersonalChange from './OperationPersonalChange'

    export default {
        name: 'Case',
        data () {
            return {
                // 查询表单
                searchForm: {
                    ascriptionCompany: '',
                    targetName: '',
                    disputeType: '',
                    caseType: '',
                    moneyStart: 0,
                    moneyEnd: 0,
                    servicePersonal: '',
                    riskGrade: '',
                    applyDate: ''
                },
                // 字典数据
                dictionary: {
                    disputeType: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.disputeType))),
                    caseType: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.caseType))),
                    riskGrade: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.riskGrade))),
                    psPersonnelList: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.psDataName))),
                    psPersonnelCacheList: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.psDataName)))
                },
                // 日期控件配置
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick (picker) {
                            const end = new Date()
                            const start = new Date()
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
                            picker.$emit('pick', [start, end])
                        }
                    }, {
                        text: '最近一个月',
                        onClick (picker) {
                            const end = new Date()
                            const start = new Date()
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
                            picker.$emit('pick', [start, end])
                        }
                    }, {
                        text: '最近三个月',
                        onClick (picker) {
                            const end = new Date()
                            const start = new Date()
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
                            picker.$emit('pick', [start, end])
                        }
                    }]
                },
                tabSelect: 'working',
                tableData: {
                    working: [],
                    closed: []
                },
                // 分页参数
                pagination: {
                    working: {
                        pageSizeList: [10, 20, 30, 40, 50],
                        pageSize: 10,
                        layout: 'total, sizes, prev, pager, next, jumper',
                        total: 0,
                        currentPage: 1
                    },
                    closed: {
                        pageSizeList: [10, 20, 30, 40, 50],
                        pageSize: 10,
                        layout: 'total, sizes, prev, pager, next, jumper',
                        total: 0,
                        currentPage: 1
                    }
                },
                addVisibleFlag: false,
                editVisibleFlag: false,
                viewVisibleFlag: false,
                remindVisibleFlag: false,
                changeServiceVisibleFlag: false,
                deletebatchList: {
                    working: [],
                    closed: [],
                    deleteFlag: false
                },
                loading: false
            }
        },
        components: {
            operationAdd,
            operationEdit,
            operationView,
            operationRemind,
            operationPersonalChange
        },
        mounted () {
            this.init()
        },
        methods: {
            init: function () {
                // TODO 加载列表数据
                this.getTableData('init')
            },
            doSearch: function () {
                this.getTableData('init')
            },
            addData: function () {
                this.addVisibleFlag = true
            },
            deleteBatch: function () {
                let _this = this
                _this.$confirm('确认删除当前选择' +
                    (this.tabSelect === 'working' ? '在办' : '结案') +
                    '的' + _this.deletebatchList[_this.tabSelect].length + '条数据？？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this.loading = true
                    _this.FUNCTIONS.systemFunction.interactiveData(
                        _this,
                        _this.GLOBAL.config.businessFlag.case,
                        _this.GLOBAL.config.handleType.deleteLogicalBatch,
                        _this.deletebatchList[_this.tabSelect],
                        'list',
                        resultData => {
                        _this.loading = false
                        if (resultData) {
                            _this.$message.success('删除成功～')
                            _this.getTableData('init')
                        } else {
                            _this.$message.warning('删除失败～')
                        }
                    }
            )
            })
            },
            getSource: function (rowData) {
                // TODO 根据uap返回数据，及rowData来进行分配
                return [
                    {icon: 'el-icon-edit', title: '编辑', method: 'handleEdit'},
                    {icon: 'el-icon-view', title: '查看', method: 'handleView'},
                    {icon: 'el-icon-delete', title: '删除', method: 'handleDelete'},
                    {icon: 'el-icon-bell', title: '提醒', method: 'handleRemind'}
                ]
            },
            handleCommon: function (type, rowData) {
                switch (type) {
                    case 'handleEdit':
                        this.handleEdit(rowData)
                        break
                    case 'handleView':
                        this.handleView(rowData)
                        break
                    case 'handleDelete':
                        this.handleDelete(rowData)
                        break
                    case 'handleRemind':
                        this.handleRemind(rowData)
                        break
                }
            },
            handleEdit: function (rowData) {
                let _this = this
                _this.editVisibleFlag = true
                setTimeout(() => {
                    _this.$refs.operationEdit.init(rowData.id)
                }, 5)
            },
            handleView: function (rowData) {
                let _this = this
                _this.viewVisibleFlag = true
                setTimeout(() => {
                    _this.$refs.operationView.init(rowData.id)
                }, 5)
            },
            handleDelete: function (rowData) {
                let _this = this
                _this.$confirm('确认删除当前数据？？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this.loading = true
                    _this.FUNCTIONS.systemFunction.interactiveData(
                        _this,
                        _this.GLOBAL.config.businessFlag.case,
                        _this.GLOBAL.config.handleType.deleteLogical,
                        rowData.id,
                        null,
                        resultData => {
                        _this.loading = false
                        if (resultData) {
                            _this.$message.success('删除成功～')
                            _this.getTableData('init')
                        } else {
                            _this.$message.warning('删除失败～')
                        }
                    }
            )
            })
            },
            handleRemind: function (rowData) {
                let _this = this
                _this.remindVisibleFlag = true
                setTimeout(() => {
                    _this.$refs.operationRemind.init(rowData.id)
                }, 5)
            },
            // 获取列表
            getTableData: function (initPageFlag) {
                this.loading = true
                // 1、获取查询条件内容
                let _this = this, searchParams = this.searchForm
                _this.FUNCTIONS.systemFunction.removeNullFields(searchParams)
                //2、 获取当前展示tab,并赋值
                let selectTab = _this.tabSelect
                searchParams.caseStatus = selectTab
                let paginationData = _this.FUNCTIONS.systemFunction.paginationSet(
                    initPageFlag ? 1 : _this.pagination[selectTab].currentPage,
                    initPageFlag ? 10 : _this.pagination[selectTab].pageSize,
                    searchParams)
                //3、 调接口获取数据
                _this.FUNCTIONS.systemFunction.interactiveData(
                    _this,
                    _this.GLOBAL.config.businessFlag.case,
                    _this.GLOBAL.config.handleType.getPage,
                    paginationData,
                    null,
                    resultData => {
                    _this.loading = false
                    if (resultData) {
                        // 结果参数赋值
                        _this.pagination[selectTab].pageSize = resultData.size
                        _this.pagination[selectTab].total = resultData.total
                        _this.pagination[selectTab].currentPage = resultData.current
                        _this.tableData[selectTab] = resultData.records
                    } else {
                        _this.$message.warning('获取列表数据失败～')
                    }
                }
            )

                // table及分页数据赋值
            },
            // tabchange事件
            tabChange: function (target) {
                let targetName = target.name
                // 列表是否重新加载
                !this.tableData[targetName].length && this.getTableData('init')
                // 删除按钮是否启用
                if (this.deletebatchList[targetName] &&
                    this.deletebatchList[targetName].length > 0) {
                    this.deletebatchList.deleteFlag = true
                } else {
                    this.deletebatchList.deleteFlag = false
                }

            },
            // 分页方法
            handleWorkingSizeChange: function (pageSize) {
                this.pagination.working.pageSize = pageSize
                this.getTableData()
            },
            handleWorkingCurrentChange: function (current) {
                this.pagination.working.currentPage = current
                this.getTableData()
            },
            handleClosedSizeChange: function (pageSize) {
                this.pagination.closed.pageSize = pageSize
                this.getTableData()
            },
            handleClosedCurrentChange: function (current) {
                this.pagination.closed.currentPage = current
                this.getTableData()
            },
            // 复选框选择事件
            handleWorkingSelectionChange: function (targetList) {
                let ids = []
                targetList.forEach(item => {
                    ids.push(item.id)
                })
                this.deletebatchList.working = ids
                if (this.deletebatchList.working &&
                    this.deletebatchList.working.length > 0) {
                    this.deletebatchList.deleteFlag = true
                } else {
                    this.deletebatchList.deleteFlag = false
                }

            },
            handleClosedSelectionChange: function (targetList) {
                let ids = []
                targetList.forEach(item => {
                    ids.push(item.id)
                })
                this.deletebatchList.closed = ids
                if (this.deletebatchList.closed &&
                    this.deletebatchList.closed.length > 0) {
                    this.deletebatchList.deleteFlag = true
                } else {
                    this.deletebatchList.deleteFlag = false
                }
            },
            modifyServicePersonal: function (rowData) {
                let _this = this
                _this.changeServiceVisibleFlag = true
                setTimeout(() => {
                    _this.$refs.operationPersonalChange.init(rowData)
                }, 5)
            },
            serviceSearch: function (targetData) {
                let temp = this.dictionary.psPersonnelCacheList
                if (targetData) {
                    this.dictionary.psPersonnelList = temp.filter(item => {
                        return (item.name === targetData) || (item.domainName === targetData)
                })
                    if (this.dictionary.psPersonnelList.length === 0) {
                        return false
                    }
                } else {
                    this.dictionary.psPersonnelList = temp
                }
            },
            // 列表数据导出
            exportTableData: function () {
                // 1、获取查询条件内容
                let _this = this, searchParams = this.searchForm
                _this.FUNCTIONS.systemFunction.removeNullFields(searchParams)
                //2、 获取当前展示tab,并赋值
                let selectTab = _this.tabSelect
                searchParams.caseStatus = selectTab
                this.FUNCTIONS.systemFunction.postDownFile(this, {
                    type: 'litigationCasesServiceImpl.downFile',
                    info: searchParams
                })
            }
        }
    }
</script>
