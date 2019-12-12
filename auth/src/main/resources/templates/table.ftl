<!--${fileDesc!}-->
<template>
    <div class="main-area">
        <el-breadcrumb separator=":">
            <el-breadcrumb-item>当前位置</el-breadcrumb-item>
            <el-breadcrumb-item>${tableComment!}</el-breadcrumb-item>
        </el-breadcrumb>
        <!--查询区域-->
        <el-row class="margin-top-10">
    <#list fields! as field>
    <#switch field.fieldType>
        <#case "normal">
            <el-col :span="3" class="margin-top-10">
                <label class="search-label">
                    ${field.filedNameCode?split("#")[0]!}:
                </label>
            </el-col>
            <el-col :span="4" class="margin-top-10">
                <el-input v-model="searchForm.${field.filedNameCode?split("#")[1]!}"
                          :size="GLOBAL.config.systemSize"
                          placeholder="${field.filedNameCode?split("#")[0]!}"
                          maxlength="32"></el-input>
            </el-col>
            <#break>
        <#case "dictionary">
            <el-col :span="3" class="margin-top-10">
                <label class="search-label">
                    ${field.filedNameCode?split("#")[0]!}:
                </label>
            </el-col>
            <el-col :span="3" class="margin-top-10">
                <el-select v-model="searchForm.${field.filedNameCode?split("#")[1]!}"
                           :size="GLOBAL.config.systemSize"
                           placeholder="${field.filedNameCode?split("#")[0]!}"
                           style="width: 100%;"
                >
                    <el-option label="--请选择--" value=""></el-option>
                    <el-option :label="item.name" :value="item.code"
                               v-for="item in dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}"
                               :key="item.id"></el-option>
                </el-select>
            </el-col>
            <#break>
        <#case "dateTime">
            <el-col :span="3" class="margin-top-10">
                <label class="search-label">
                    ${field.filedNameCode?split("#")[0]!}:
                </label>
            </el-col>
            <el-col :span="5" class="margin-top-10">
                <el-date-picker
                        v-model="searchForm.${field.filedNameCode?split("#")[1]!}"
                        :size="GLOBAL.config.systemSize"
                        type="daterange"
                        align="right"
                        unlink-panels
                        format="${(field.remark?length gt 0)?string(field.remark,"yyyy-MM-dd")}"
                        value-format="${(field.remark?length gt 0)?string(field.remark,"yyyy-MM-dd")}"
                        range-separator="至"
                        start-placeholder="开始"
                        end-placeholder="结束"
                        style="width: 100%;">
                </el-date-picker>
            </el-col>
            <#break>
        <#default>
            <el-col :span="3" class="margin-top-10">
                <label class="search-label">
                    ${field.filedNameCode?split("#")[0]!}:
                </label>
            </el-col>
            <el-col :span="4" class="margin-top-10">
                <el-input v-model="searchForm.${field.filedNameCode?split("#")[1]!}"
                          :size="GLOBAL.config.systemSize"
                          placeholder="${field.filedNameCode?split("#")[0]!}"
                          maxlength="32"></el-input>
            </el-col>
    </#switch>
    </#list>
            <#--导入导出查询按钮-->
            <el-col :span="2" class="margin-top-10">
                <el-button type="primary" @click="doSearch" :size="GLOBAL.config.systemSize" icon="el-icon-search">查询
                </el-button>
            </el-col>
        </el-row>
        <el-row class="margin-top-20">
            <#--列表操作按钮-->
            <el-col :span="24" style="float: left;">
    <#list tableHandleList! as tableHandle>
        <#switch tableHandle>
            <#case "deleteBatch">
                <el-button v-if="source.deleteBatch"
                           type="danger"
                           icon="el-icon-delete"
                           :size="GLOBAL.config.systemSize"
                           @click="deleteBatch">批量删除
                </el-button>
                <#break>
            <#case "addInfo">
                <el-button v-if="source.add"
                           type="primary"
                           icon="el-icon-plus"
                           :size="GLOBAL.config.systemSize"
                           @click="operationMethod('add')">新增
                </el-button>
                <#break>
            <#case "export">
                <el-button v-if="source.export"
                           type="primary"
                           icon="el-icon-download"
                           :size="GLOBAL.config.systemSize"
                           @click="exportTableData">导出
                </el-button>
                <#break>
            <#case "import">
                <el-button v-if="source.import"
                           type="primary"
                           icon="el-icon-download"
                           :size="GLOBAL.config.systemSize"
                           @click="importTableData">导入
                </el-button>
                <#break>
        </#switch>
    </#list>
            </el-col>
        </el-row>
        <el-table style="width: 100%"
                  :data="tableData.working"
                  @selection-change="tableSelectionChange"
                  element-loading-text="数据处理中...请稍等..."
                  v-loading="loading">
            <#list tableHandleList! as tableHandle>
                <#switch tableHandle>
                    <#case "deleteBatch">
            <el-table-column
                    type="selection">
            </el-table-column>
                        <#break>
                </#switch>
            </#list>
            <#list fields! as field>
                <#switch field.fieldType>
                    <#case "normal">
            <!--${field.filedNameCode?split("#")[3]!}-->
            <el-table-column prop="${field.filedNameCode?split("#")[1]!}" label="${field.filedNameCode?split("#")[0]!}" align="center"/>
                        <#break>
                    <#case "dictionary">
            <!--${field.filedNameCode?split("#")[3]!}-->
            <el-table-column prop="${field.filedNameCode?split("#")[1]!}"
                             label="${field.filedNameCode?split("#")[0]!}" align="center">
                <template slot-scope="scope">
                    {{
                    FUNCTIONS.systemFunction.getConfigValue(
                    scope.row.${field.filedNameCode?split("#")[1]!},
                    GLOBAL.config.dictionaryPre +
                    GLOBAL.config.dictionary.${field.filedNameCode?split("#")[0]!})
                    }}
                </template>
            </el-table-column>
                        <#break>
                    <#case "dateTime">
            <!--${field.filedNameCode?split("#")[3]!}-->
            <el-table-column prop="${field.filedNameCode?split("#")[1]!}"
                             label="${field.filedNameCode?split("#")[0]!}">
                <template slot-scope="scope">
                    {{ scope.row.${field.filedNameCode?split("#")[1]!}
                    ?$moment(scope.row.${field.filedNameCode?split("#")[1]!}
                    ).format('${(field.remark?length gt 0)?string(field.remark,"yyyy-MM-dd")}'):'' }}
                </template>
            </el-table-column>
                        <#break>
                    <#default>
            <!--${field.filedNameCode?split("#")[3]!}-->
            <el-table-column prop="${field.filedNameCode?split("#")[1]!}" label="${field.filedNameCode?split("#")[0]!}" align="center"/>
                </#switch>
            </#list>
            <el-table-column prop="scope" label="操作" align="center">
                <template slot-scope="scope">
                    <el-dropdown>
                <span class="el-dropdown-link operator-text">
                  选择操作<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item :icon="item.icon" v-for="item in getSource(scope.row)"
                                              :key="item.method"
                                              @click.native="handleCommon(item.method, scope.row)">
                                {{item.title}}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
                class="margin-top-10 margin-bottom-20"
                @size-change="tableSizeChange"
                @current-change="currentChange"
                :current-page="pagination.currentPage"
                :page-sizes="pagination.pageSizeList"
                :page-size="pagination.pageSize"
                :layout="pagination.layout"
                :total="pagination.total">
        </el-pagination>
        <!--操作-->
        <el-dialog
                :fullscreen="true"
                :show-close="false"
                :visible.sync="operationVisibleFlag"
                :destroy-on-close="true">
            <operationTemplate
                    ref="operationTemplate"
                    :refresh="init"
                    :close-self="()=> operationVisibleFlag = false"
            />
        </el-dialog>
    </div>
</template>
<script>
    // 替换成相应的模板
    import operationTemplate from './${tableName!}Table'

    export default {
        name: '${tableName!}',
        data() {
            return {
                // 查询表单
                searchForm: {
                    <#list fields! as field>
                    ${field.filedNameCode?split("#")[1] + ": '',"}
                    </#list>
                },
                // 字典数据
                dictionary: {
                <#list fields! as field>
                <#switch field.fieldType>
                <#case "dictionary">
                ${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}:
                    JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}))),
                <#break>
                </#switch>
                </#list>
                },
                // 资源权限控制，有的系统不需这么细，则全部为true
                source: {
                    deleteBatch: true,
                    add: true,
                    import: true,
                    export: true
                },
                // 分页参数
                pagination: {
                    pageSizeList: [10, 20, 30, 40, 50],
                    pageSize: 10,
                    layout: 'total, sizes, prev, pager, next, jumper',
                    total: 0,
                    currentPage: 1
                },
                operationVisibleFlag: false,
                <#list tableHandleList! as tableHandle>
                <#switch tableHandle>
                <#case "deleteBatch">
                deleteBatchList: {
                    ids: [],
                    deleteFlag: false
                },
                <#break>
                </#switch>
                </#list>
                loading: false
            }
        },
        components: {
            operationTemplate
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
            operationMethod: function (operateType, info) {
                this.operationVisibleFlag = true
            },
            <#list tableHandleList! as tableHandle>
            <#switch tableHandle>
            <#case "deleteBatch">
            deleteBatch: function () {
                let _this = this
                _this.$confirm('确认删除当前选择' +
                    '的' + _this.deleteBatchList.ids.length + '条数据？？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    _this.loading = true
                    _this.FUNCTIONS.systemFunction.interactiveData(
                        _this,
                        _this.GLOBAL.config.businessFlag.${tableName!},
                        _this.GLOBAL.config.handleType.deleteLogicalBatch,
                        _this.deleteBatchList.ids,
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
            <#break>
            </#switch>
            </#list>
            getSource: function (rowData) {
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
                        this.operationMethod('edit', rowData)
                        break
                    case 'handleView':
                        this.operationMethod('view', rowData)
                        break
                    case 'handleDelete':
                        this.handleDelete(rowData)
                        break
                }
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
                        _this.GLOBAL.config.businessFlag.${tableName!},
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
            // 获取列表
            getTableData: function (initPageFlag) {
                this.loading = true
                let _this = this, searchParams = this.searchForm
                _this.FUNCTIONS.systemFunction.removeNullFields(searchParams)
                let paginationData = _this.FUNCTIONS.systemFunction.paginationSet(
                    initPageFlag ? 1 : _this.pagination.currentPage,
                    initPageFlag ? 10 : _this.pagination.pageSize,
                    searchParams)
                // 3、 调接口获取数据
                _this.FUNCTIONS.systemFunction.interactiveData(
                    _this,
                    _this.GLOBAL.config.businessFlag.${tableName!},
                    _this.GLOBAL.config.handleType.getPage,
                    paginationData,
                    null,
                    resultData => {
                    _this.loading = false
                    if (resultData) {
                        // 结果参数赋值
                        _this.pagination.pageSize = resultData.size
                        _this.pagination.total = resultData.total
                        _this.pagination.currentPage = resultData.current
                        _this.tableData = resultData.records
                    } else {
                        _this.$message.warning('获取列表数据失败～')
                    }
                }
            )
            },
            // 分页方法
            tableSizeChange: function (pageSize) {
                this.pagination.pageSize = pageSize
                this.getTableData()
            },
            currentChange: function (current) {
                this.pagination.currentPage = current
                this.getTableData()
            },
            <#list tableHandleList! as tableHandle>
            <#switch tableHandle>
            <#case "deleteBatch">
            // 复选框选择事件
            tableSelectionChange: function (targetList) {
                let ids = []
                targetList.forEach(item => {
                    ids.push(item.id)
                })
                this.deleteBatchList.ids = ids
                if (this.deleteBatchList.ids &&
                    this.deleteBatchList.ids.length > 0) {
                    this.deleteBatchList.deleteFlag = true
                } else {
                    this.deleteBatchList.deleteFlag = false
                }
            },
            <#break>
            </#switch>
            </#list>
            <#list tableHandleList! as tableHandle>
            <#switch tableHandle>
            <#case "export">
            // 列表数据导出
            exportTableData: function () {
                let _this = this, searchParams = this.searchForm
                _this.FUNCTIONS.systemFunction.removeNullFields(searchParams)
                this.FUNCTIONS.systemFunction.postDownFile(this, {
                    type: 'litigationCasesServiceImpl.downFile',
                    info: searchParams
                })
            },
            <#break>
            </#switch>
            </#list>
            <#list tableHandleList! as tableHandle>
            <#switch tableHandle>
            <#case "import">
            importTableData: function () {
                // TODO
            }
            <#break>
            </#switch>
            </#list>
        }
    }
</script>
