<!--${fileDesc!}-->
<template>
    <el-dialog :title="showTitle" :visible.sync="showFlag">
        <el-form
                :inline="true"
                :model="formData"
                class="demo-ruleForm"
                label-width="100px"
                :rules="formRules"
                ref="formData"
                :size="GLOBAL.config.systemSize"
                element-loading-text="数据处理中...请稍等..."
                <#if viewFlag??>
                :disabled="!editableFlag"
                </#if>
                v-loading="loading">
            <el-row class="margin-top-20">
                <#list fields! as field>
                <#switch field.fieldType>
                <#case "input">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-input v-model="formData.${field.filedNameCode?split("#")[1]!}" placeholder="${field.filedNameCode?split("#")[0]!}" maxlength="64"></el-input>
                    </el-form-item>
                </el-col>
                <#break>
                <#case "radio">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-radio-group
                                v-model="formData.${field.filedNameCode?split("#")[1]!}"
                                placeholder="${field.filedNameCode?split("#")[0]!}">
                            <el-radio
                                    v-for="item in dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}"
                                    :label="item.code"
                                    :key="item.id">{{item.name}}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-col>
                <#break>
                <#case "checkbox">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-checkbox-group
                                v-model="formData.${field.filedNameCode?split("#")[1]!}"
                                placeholder="${field.filedNameCode?split("#")[0]!}"
                                style="width: 100%;">
                            <el-checkbox
                                    :label="item.name"
                                    :true-label="item.code"
                                    v-for="item in dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}"
                                    :key="item.id">
                            </el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>
                </el-col>
                <#break>
                <#case "inputNumber">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-input-number
                                v-model="formData.${field.filedNameCode?split("#")[1]!}"
                                placeholder="${field.filedNameCode?split("#")[0]!}"
                                :min="0"
                                :precision="2"
                                :step="1"
                                style="width: 100%;">
                        </el-input-number>
                    </el-form-item>
                </el-col>
                <#break>
                <#case "select">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-select v-model="formData.${field.filedNameCode?split("#")[1]!}"
                                   placeholder="${field.filedNameCode?split("#")[0]!}"
                                   style="width: 100%;"
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name" :value="item.code"
                                       v-for="item in dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <#break>
                <#case "switch">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-switch
                                style="float: left;"
                                v-model="formData.${field.filedNameCode?split("#")[1]!}"
                                inactive-color="#13ce66"
                                active-color="#ececec"
                                active-value="dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}[0].code"
                                inactive-value="dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}[1].code"
                                active-text="dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}[0].name"
                                inactive-text="dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}[1].name"
                        >
                        </el-switch>
                    </el-form-item>
                </el-col>
                <#break>
                <#case "date">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-date-picker
                                style="width: 100%"
                                v-model="formData.${field.filedNameCode?split("#")[1]!}"
                                type="date"
                                format="${(field.remark?length gt 0)?string(field.remark,"yyyy-MM-dd")}"
                                value-format="${(field.remark?length gt 0)?string(field.remark,"yyyy-MM-dd")}"
                                placeholder="选择${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
                <#break>
                <#case "time">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-time-picker
                                style="width: 100%"
                                v-model="formData.${field.filedNameCode?split("#")[1]!}"
                                type="date"
                                format="${(field.remark?length gt 0)?string(field.remark,"HH:mm:ss")}"
                                value-format="${(field.remark?length gt 0)?string(field.remark,"HH:mm:ss")}"
                                placeholder="选择${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        </el-time-picker>
                    </el-form-item>
                </el-col>
                <#break>
                <#case "dateTime">
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-date-picker
                                style="width: 100%"
                                v-model="formData.${field.filedNameCode?split("#")[1]!}"
                                type="date"
                                format="${(field.remark?length gt 0)?string(field.remark,"yyyy-MM-dd HH:mm:ss")}"
                                value-format="${(field.remark?length gt 0)?string(field.remark,"yyyy-MM-dd HH:mm:ss")}"
                                placeholder="选择${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
                <#break>
                <#default>
                <el-col :span="${field.fieldWidth?default(4)}">
                    <el-form-item label="${field.filedNameCode?split("#")[0]!}：" prop="${field.filedNameCode?split("#")[1]!}">
                        <el-input v-model="formData.${field.filedNameCode?split("#")[1]!}" placeholder="${field.filedNameCode?split("#")[0]!}" maxlength="64"></el-input>
                    </el-form-item>
                </el-col>
                </#switch>
                </#list>
                <#if enclosureFlag??>
                <el-col :span="24">
                    <el-form-item label="附件：">
                        <el-upload
                                style="float: left;"
                                class="upload-demo"
                                list-type="picture-card"
                                :action="CONFIG.urls.root +  CONFIG.urls.system.upload"
                                :on-success="fileUpload"
                                :file-list="formData.filesCache"
                                ref="upload"
                                multiple>
                            <i slot="default" class="el-icon-plus"></i>
                            <div slot="file" slot-scope="{file}" class="upload-view">
                                <img
                                        style="display: block;"
                                        class="el-upload-list__item-thumbnail"
                                        :src="FUNCTIONS.systemFunction.fileTypeView(this, file)">
                                <span class="el-upload-list__item-actions">
                                        <span
                                                class="el-upload-list__item-delete"
                                                @click="FUNCTIONS.systemFunction.fileDownload(file)">
                                          <i class="el-icon-download"></i>
                                        </span>
                                        <span class="el-upload-list__item-delete"
                                              @click="fileRemove(file)">
                                          <i class="el-icon-delete"></i>
                                        </span>
                                      </span>
                                <span>{{file.response.data.fileName}}</span>
                            </div>
                        </el-upload>
                    </el-form-item>
                </el-col>
                </#if>
            </el-row>
        </el-form>
        <el-row class="margin-top-20">
            <el-button @click="closeDialog" style="margin: 0 20px;" :size="GLOBAL.config.systemSize">关闭</el-button>
            <#if saveFlag?? || updateFlag??>
                <el-button v-if="editableFlag" type="primary" @click="saveOrUpdateForm" style="margin: 0 20px;" :size="GLOBAL.config.systemSize">保存
                </el-button>
            </#if>
            <#if submitFlag??>
                <el-button type="primary" @click="submitForm" style="margin: 0 20px;" :size="GLOBAL.config.systemSize">提交</el-button>
            </#if>
            <#if approvalFlag??>
                <el-button type="primary" @click="approvalForm" style="margin: 0 20px;" :size="GLOBAL.config.systemSize">审批</el-button>
            </#if>
        </el-row>
    </el-dialog>
</template>
<script>export default {
        name: '${tableName!}',
        props: {
            id: {
                type: String
            },
            refresh: {
                type: Function
            },
            closeSelf: {
                type: Function
            }
        },
        data () {
            return {
                formData: {
                    <#if updateFlag??||viewFlag??>
                    id: '',
                    </#if>
                    <#list fields! as field>
                    ${field.filedNameCode?split("#")[1] + ": '',"}
                    </#list>
                    <#if enclosureFlag??>
                    filesCache: []
                    </#if>
                },
                // 校验规则
                formRules: {
                    <#list fields! as field>
                    ${field.filedNameCode?split("#")[1] + ": [],"}
                    </#list>
                },
                // 字典数据
                dictionary: {
                    <#list fields! as field>
                    <#switch field.fieldType>
                    <#case "radio">
                    <#case "checkbox">
                    <#case "select">
                    <#case "switch">
                    ${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.${(field.remark?length gt 0)?string(field.remark, field.filedNameCode?split("#")[1])}))),
                    <#break>
                    </#switch>
                    </#list>
                },
                <#if viewFlag??>
                editableFlag: true,
                </#if>
                loading: false,
                showTitle: '新增',
                showFlag: false
            }
        },
        methods: {
            init: function (type, id) {
                let _title = ''
                if (type === 'add') {
                    _title = '新增'
                } else if (type === 'edit') {
                    _title = '编辑'
                } else if (type === 'view') {
                    _title = '查看'
                    this.editableFlag = false
                }
                this.showTitle = this.showTitle || _title
                this.showFlag = true
                if (id) {
                    this.formData.id = id
                    this.getInfo()
                }
            },
            closeDialog: function () {
                this.showFlag = false
            },
            saveOrUpdateForm: function () {
                this.formData.id ? this.updateForm() : this.saveForm()
            },
            <#if saveFlag??>
            saveForm: function () {
                this.$refs.formData.validate(valid => {
                    if (valid) {
                        let params = this.formData
                        // 参数处理======start==========
                        // TODO 对于单选、复选、多选、附件等需要进行单独处理
                        // 参数处理======end============
                        let _this = this
                        _this.loading = true
                        this.FUNCTIONS.systemFunction.interactiveData(
                            _this,
                            _this.GLOBAL.config.businessFlag.${tableName!},
                            _this.GLOBAL.config.handleType.add,
                            _this.FUNCTIONS.systemFunction.removeNullFields(params),
                            null,
                            resultData => {
                                _this.loading = false
                                if (resultData) {
                                    _this.$message.success('保存成功～')
                                    _this.showFlag = false
                                    _this.$props.refresh && this.$props.refresh('init')
                                } else {
                                    _this.$message.warning('保存失败～')
                                }
                            },
                            () => {
                                _this.loading = false
                            })
                    } else {
                        this.$message.error('校验失败～')
                        return false
                    }
                })
            },
            </#if>
            <#if updateFlag??>
            updateForm: function () {
                this.$refs.formData.validate(valid => {
                    if (valid) {
                        let params = this.formData
                        // 参数处理======start==========
                        // TODO 对于单选、复选、多选、附件等需要进行单独处理
                        // 参数处理======end============
                        let _this = this
                        _this.loading = true
                        this.FUNCTIONS.systemFunction.interactiveData(
                            _this,
                            _this.GLOBAL.config.businessFlag.${tableName!},
                            _this.GLOBAL.config.handleType.updateAll,
                            _this.FUNCTIONS.systemFunction.removeNullFields(params),
                            null,
                            resultData => {
                                _this.loading = false
                                if (resultData) {
                                    _this.$message.success('修改成功～')
                                    _this.showFlag = false
                                    _this.$props.refresh && this.$props.refresh('init')
                                } else {
                                    _this.$message.warning('修改失败～')
                                }
                            },
                            () => {
                                _this.loading = false
                            })
                    } else {
                        this.$message.error('校验失败～')
                        return false
                    }
                })
            },
            </#if>
            <#if updateFlag??||viewFlag??>
            getInfo: function () {
                let _this = this
                this.formData.id && this.FUNCTIONS.systemFunction.interactiveData(
                    _this,
                    _this.GLOBAL.config.businessFlag.${tableName!},
                    _this.GLOBAL.config.handleType.getInfoById,
                    _this.formData.id,
                    null,
                    resultData => {
                        _this.loading = false
                        <#if enclosureFlag??>
                        // 附件数据处理
                        if (resultData.files && resultData.files.length > 0) {
                            resultData.files.forEach(item => {
                                _this.formData.filesCache.push({
                                    response: {
                                        code: '0',
                                        data: item
                                    }
                                })
                            })
                        }
                        </#if>
                        Object.assign(_this.formData, resultData)
                    }
                )
            },
            </#if>
            <#if enclosureFlag??>
            fileUpload: function (response, file, fileList) {
                if (response && (response.code !== this.GLOBAL.config.resultCode.success)) {
                    this.$message.error('上传失败～')
                } else {
                    this.formData.files.push(response.data)
                }
            },
            fileRemove: function (file) {
                let eg = file.response.data
                this.formData.files = this.formData.files.filter(item => (item.fileUrl !== eg.fileUrl)
            )
                let fileList = this.$refs.upload.uploadFiles
                let index = fileList.findIndex(fileItem => {
                    return fileItem.uid === file.uid
                }
            )
                fileList.splice(index, 1)
            }
            </#if>
            <#if submitFlag??>
            // 提交
            submitForm: function () {
                // TODO
            }
            </#if>
            <#if approvalFlag??>
            // 审批
            approvalForm: function () {
                // TODO
            }
            </#if>
        }
    }
</script>
