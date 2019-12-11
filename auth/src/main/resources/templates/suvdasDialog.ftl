<template>
    <div class="main-area">
        <el-page-header @back="goBack" content="诉讼类案件新增"></el-page-header>
        <el-form
                :inline="true"
                :model="formData"
                class="demo-ruleForm"
                label-width="145px"
                :rules="formRules"
                ref="formData"
                :size="GLOBAL.config.systemSize"
                element-loading-text="数据处理中...请稍等..."
                v-loading="loading"
        >
            <el-row class="margin-top-20">
                <el-col :span="8">
                    <el-form-item label="流程编号：" prop="flowNo">
                        <el-input v-model="formData.flowNo" placeholder="流程编号" maxlength="64"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="申请日期：" prop="applyDate">
                        <el-date-picker
                                style="width: 100%"
                                v-model="formData.applyDate"
                                type="date"
                                format="yyyy-MM-dd"
                                placeholder="选择申请日期">
                        </el-date-picker>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="纠纷发生法人主体：" prop="ascriptionCompany">
                        <el-input v-model="formData.ascriptionCompany" placeholder="纠纷发生法人主体" maxlength="64"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="业务所在部门：" prop="unitName">
                        <el-input v-model="formData.unitName" placeholder="业务所在部门" maxlength="64"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="申请人：" prop="applicant">
                        <el-input v-model="formData.applicant" placeholder="申请人" maxlength="16"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="对方名称：" prop="targetName">
                        <el-input v-model="formData.targetName" placeholder="对方名称" maxlength="64"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="金额：" prop="money">
                        <el-input-number
                                v-model="formData.money"
                                :min="0"
                                :precision="2"
                                :step="0.5"
                                style="width: 100%;"
                                placeholder="金额">
                        </el-input-number>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="案件类型：" prop="disputeType">
                        <el-select v-model="formData.caseType"
                                   placeholder="案件类型"
                                   style="width: 100%;"
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name" :value="item.code"
                                       v-for="item in dictionary.caseType" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="纠纷类型：" prop="disputeType">
                        <el-select v-model="formData.disputeType"
                                   placeholder="纠纷类型"
                                   style="width: 100%;"
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name" :value="item.code"
                                       v-for="item in dictionary.disputeType" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="服务类型：" prop="serviceType">
                        <el-select v-model="formData.serviceType"
                                   placeholder="服务类型"
                                   style="width: 100%;"
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name" :value="item.code"
                                       v-for="item in dictionary.serviceType" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="服务人员：" prop="servicePersonal">
                        <el-select v-model="formData.servicePersonal"
                                   placeholder="服务人员"
                                   style="width: 100%;"
                                   filterable
                                   :filter-method="serviceSearch"
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name +'(' + item.domainName + ')'" :value="item.id"
                                       v-for="item in dictionary.psPersonnelList" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="16">
                    <el-form-item label="协作人员：" prop="cooperationPersonnel">
                        <el-select v-model="formData.cooperationPersonnel"
                                   placeholder="协作人员"
                                   style="width: 100%;"
                                   filterable
                                   multiple
                                   :filter-method="serviceSearch"
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name +'(' + item.domainName + ')'" :value="item.id"
                                       v-for="item in dictionary.psPersonnelList" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="诉求：" prop="appeal">
                        <el-select v-model="formData.appeal"
                                   placeholder="诉求"
                                   style="width: 100%;"
                                   filterable
                                   multiple
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name" :value="item.code"
                                       v-for="item in dictionary.appealType" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="16">
                    <el-form-item label="其他诉求：" prop="otherAppeal">
                        <el-input v-model="formData.otherAppeal" placeholder="其他诉求" maxlength="1024"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="24">
                    <el-form-item label="案件简介：" prop="caseBrief">
                        <el-input
                                type="textarea"
                                autosize
                                placeholder="请输入案件简介"
                                :maxlength="GLOBAL.config.textMaxlength.oneThousand"
                                v-model="formData.caseBrief">
                        </el-input>
                    </el-form-item>
                    <span class="tips-max-length"
                          :style="(formData.caseBrief && (formData.caseBrief.length === GLOBAL.config.textMaxlength.oneThousand))?'color: red;':''">{{formData.caseBrief?formData.caseBrief.length:0}}/{{GLOBAL.config.textMaxlength.oneThousand}}</span>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="是否外聘律师：" prop="outsideLawyerFlag">
                        <el-select v-model="formData.outsideLawyerFlag"
                                   placeholder="是否外聘律师"
                                   style="width: 100%;"
                                   @change="outsideLawyerFlagChange"
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name" :value="item.code"
                                       v-for="item in dictionary.outsideLawerFlag" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="律所名称：" prop="lawFirmName"
                                  :rules="outsideLawyerYes?formRules.lawFirmName:[{required: false, message: '律所名称不可为空', trigger: 'blur'}]">
                        <el-input v-model="formData.lawFirmName" placeholder="律所名称" maxlength="64"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="律师姓名：" prop="lawerName"
                                  :rules="outsideLawyerYes?formRules.lawerName:[{required: false, message: '律师姓名不可为空', trigger: 'blur'}]">
                        <el-input v-model="formData.lawerName" placeholder="律师姓名" maxlength="16"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="是否产生费用：" prop="costFlag">
                        <el-select v-model="formData.costFlag"
                                   placeholder="是否产生费用"
                                   style="width: 100%;"
                                   @change="costFlagChange"
                        >
                            <el-option label="--请选择--" value=""></el-option>
                            <el-option :label="item.name" :value="item.code"
                                       v-for="item in dictionary.costFlag" :key="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="费用总额：" prop="costAmount">
                        <el-input-number
                                v-model="formData.costAmount"
                                :min="0"
                                :precision="2"
                                :step="0.5"
                                style="width: 100%;"
                                placeholder="费用总额"
                                v-bind:disabled="costDisabledInput">
                        </el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="保全费：" prop="preservationAmount">
                        <el-input-number
                                v-model="formData.preservationAmount"
                                :min="0"
                                :precision="2"
                                :step="0.5"
                                style="width: 100%;"
                                placeholder="保全费"
                                v-bind:disabled="costDisabledInput">
                        </el-input-number>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="8">
                    <el-form-item label="代理费：" prop="agentAmount">
                        <el-input-number
                                v-model="formData.agentAmount"
                                :min="0"
                                :precision="2"
                                :step="0.5"
                                style="width: 100%;"
                                placeholder="代理费"
                                v-bind:disabled="costDisabledInput">
                        </el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="诉讼费：" prop="litigationAmount">
                        <el-input-number
                                v-model="formData.litigationAmount"
                                :min="0"
                                :precision="2"
                                :step="0.5"
                                style="width: 100%;"
                                placeholder="诉讼费"
                                v-bind:disabled="costDisabledInput">
                        </el-input-number>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item label="其他费用：" prop="otherAmount">
                        <el-input-number
                                v-model="formData.otherAmount"
                                :min="0"
                                :precision="2"
                                :step="0.5"
                                style="width: 100%;"
                                placeholder="其他费用"
                                v-bind:disabled="costDisabledInput">
                        </el-input-number>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row v-for="(item, index) in formData.hearInformationList" :key="item.id">
                <el-divider content-position="left">
                    审理信息{{index + 1}}
                    <i
                            v-if="index > 0"
                            class="el-icon-delete"
                            style="cursor: pointer; margin-left: 30px;"
                            title="删除"
                            :size="GLOBAL.config.systemSize"
                            @click="removeHearProcess(item)"
                    ></i>
                    <i
                            v-if="(index === formData.hearInformationList.length - 1) && (index < GLOBAL.config.limitNum.hear - 1)"
                            class="el-icon-plus"
                            style="cursor: pointer; margin-left: 10px;"
                            title="添加"
                            :size="GLOBAL.config.systemSize"
                            @click="addHearProcess"
                    ></i>
                </el-divider>
                <hearInformation :info-data="item" :key="Date.now()" ref="hearInformation"/>
                <el-divider></el-divider>
            </el-row>
            <el-row>
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
                                        :src="FUNCTIONS.systemFunction.fileTypeView(this, file)"
                                >
                                <span class="el-upload-list__item-actions">
                <span
                        class="el-upload-list__item-delete"
                        @click="FUNCTIONS.systemFunction.fileDownload(file)"
                >
                  <i class="el-icon-download"></i>
                </span>
                <span
                        class="el-upload-list__item-delete"
                        @click="fileRemove(file)"
                >
                  <i class="el-icon-delete"></i>
                </span>
              </span>
                                <span>{{file.response.data.fileName}}</span>
                            </div>
                        </el-upload>
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row class="margin-top-20">
                <el-button @click="goBack" style="float: right;margin-left: 20px;" :size="GLOBAL.config.systemSize">返回
                </el-button>
                <el-button type="primary" @click="saveForm" style="float: right;" :size="GLOBAL.config.systemSize">保存
                </el-button>

            </el-row>
        </el-form>
    </div>
</template>
<script>
    import hearInformation from './HearInformation'

    export default {
        name: 'OperationAdd',
        props: {
            id: {
                type: String,
            },
            refresh: {
                type: Function
            },
            closeSelf: {
                type: Function
            }
        },
        data() {
            return {
                formData: {
                    flowNo: '',
                    applyDate: '',
                    ascriptionCompany: '',
                    unitName: '',
                    applicant: '',
                    targetName: '',
                    money: 0,
                    caseType: '',
                    disputeType: '',
                    serviceType: '',
                    servicePersonal: '',
                    cooperationPersonnel: '',
                    appeal: '',
                    otherAppeal: '',
                    caseBrief: '',
                    outsideLawyerFlag: 'no',
                    lawFirmName: '',
                    lawerName: '',
                    costFlag: 'no',
                    costAmount: 0,
                    preservationAmount: 0,
                    agentAmount: 0,
                    litigationAmount: 0,
                    otherAmount: 0,
                    preliminaryOpinion: '',
                    riskGrade: '',
                    derogationAmount: 0,
                    // 审理信息
                    hearInformationList: [
                        {
                            id: '',
                            relationId: '',
                            hearingOrgan: '',
                            hearingProcedure: '',
                            filingDate: '',
                            openDate: '',
                            caseAnalysis: '',
                            preliminaryOpinion: '',
                            caseProcess: [{content: ''}]
                        }
                    ],
                    files: [],
                    filesCache: []
                },
                //校验规则
                formRules: {
                    flowNo: [
                        {required: true, message: '请输入流程编号', trigger: 'blur'}
                    ],
                    applyDate: [
                        {required: true, message: '请选择申请日期', trigger: 'blur'}
                    ],
                    ascriptionCompany: [
                        {required: true, message: '请输入纠纷发生法人主体', trigger: 'blur'}
                    ],
                    unitName: [
                        {required: true, message: '请输入业务所在部门', trigger: 'blur'}
                    ],
                    applicant: [
                        {required: true, message: '请输入申请人', trigger: 'blur'}
                    ],
                    targetName: [
                        {required: true, message: '请输入对方名称', trigger: 'blur'}
                    ],
                    money: [
                        {required: true, message: '请输入金额', trigger: 'blur'}
                    ],
                    disputeType: [
                        {required: true, message: '请选择纠纷类型', trigger: 'blur'}
                    ],
                    serviceType: [
                        {required: true, message: '请选择服务类型', trigger: 'blur'}
                    ],
                    servicePersonal: [
                        {required: true, message: '请选择服务人员', trigger: 'blur'}
                    ],
                    appeal: [
                        {required: true, message: '请选择诉求', trigger: 'blur'}
                    ],
                    caseBrief: [
                        {required: true, message: '请输入案件简介', trigger: 'blur'}
                    ],
                    outsideLawyerFlag: [
                        {required: true, message: '请选择是否外聘律师', trigger: 'blur'}
                    ],
                    lawFirmName: [
                        {required: true, message: '律所名称不可为空', trigger: 'blur'}
                    ],
                    lawerName: [
                        {required: true, message: '律师姓名不可为空', trigger: 'blur'}
                    ],
                    costFlag: [
                        {required: true, message: '请选择是否产生费用', trigger: 'blur'}
                    ],
                },
                // 字典数据
                dictionary: {
                    caseType: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.caseType))),
                    disputeType: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.disputeType))),
                    serviceType: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.serviceType))),
                    appealType: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.appealType))),
                    outsideLawerFlag: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.outsideLawerFlag))),
                    costFlag: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.dictionaryPre + this.GLOBAL.config.dictionary.costFlag))),
                    psPersonnelList: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.psDataName))),
                    psPersonnelCacheList: JSON.parse(unescape(localStorage.getItem(this.GLOBAL.config.psDataName))),
                },
                costDisabledInput: true,
                outsideLawyerYes: false,
                loading: false
            }
        },
        mounted() {
            this.init()
        },
        components: {
            hearInformation
        },
        methods: {
            init: function () {

            },
            goBack: function () {
                this.$props.closeSelf && this.$props.closeSelf()
            },
            serviceSearch: function (targetData) {
                let temp = this.dictionary.psPersonnelCacheList
                if (targetData) {
                    this.dictionary.psPersonnelList = temp.filter(item = > {
                        return(item.name === targetData
                ) ||
                    (item.domainName === targetData)
                })
                    if (this.dictionary.psPersonnelList.length === 0) {
                        return false
                    }
                } else {
                    this.dictionary.psPersonnelList = temp
                }
            },
            removeHearProcess(item) {
                let index = this.formData.hearInformationList.indexOf(item)
                if (index !== -1) {
                    this.formData.hearInformationList.splice(index, 1)
                }
            },
            addHearProcess() {
                for (let i = 0; i < this.formData.hearInformationList.length; i++) {
                    this.formData.hearInformationList[i] = this.$refs.hearInformation[i].getData()
                }
                this.formData.hearInformationList.push({
                    id: '',
                    relationId: '',
                    hearingOrgan: '',
                    hearingProcedure: '',
                    filingDate: '',
                    openDate: '',
                    caseAnalysis: '',
                    preliminaryOpinion: '',
                    caseProcess: [{content: ''}]
                })
            },
            saveForm: function () {
                this.$refs.formData.validate(valid = > {
                    if(valid) {
                        let params = this.formData
                        // 参数处理======start==========
                        ////////// 1、诉求，多选
                        if (params.appeal && params.appeal.length > 0) {
                            params.appeal = params.appeal.join(',')
                        }
                        ////////// 2、审理信息,涉及多次嵌套，所以需要处理下
                        for (let i = 0; i < this.formData.hearInformationList.length; i++) {
                            if (this.$refs.hearInformation[i].getData('validate')) {
                                this.formData.hearInformationList[i] = this.$refs.hearInformation[i].getData('validate')
                            } else {
                                return
                            }
                        }
                        ////////// 3、删除附件缓存数据
                        delete params.filesCache
                        // 参数处理======end============

                        let _this = this
                        _this.loading = true

                        this.FUNCTIONS.systemFunction.interactiveData(
                            _this,
                            _this.GLOBAL.config.businessFlag.case,
                            _this.GLOBAL.config.handleType.add,
                            _this.FUNCTIONS.systemFunction.removeNullFields(this.formData),
                            null,
                            resultData = > {
                            _this.loading = false
                            if(resultData) {
                                _this.$props.closeSelf && _this.$props.closeSelf()
                                _this.$props.refresh && _this.$props.refresh()
                                _this.$message.success('保存成功～')
                            } else {
                                _this.$message.warning('保存失败～')
                            }
                        }
                    )
                    } else {
                        this.$message.error('校验失败～')
                        return false
                    }
                }
            )
            },
            fileUpload: function (response, file, fileList) {
                if (response && (response.code != this.GLOBAL.config.resultCode.success)) {
                    this.$message.error('上传失败～')
                } else {
                    this.formData.files.push(response.data)
                }
            },
            fileRemove: function (file) {
                let eg = file.response.data
                this.formData.files = this.formData.files.filter(item = > (item.fileUrl !== eg.fileUrl)
            )
                let fileList = this.$refs.upload.uploadFiles;
                let index = fileList.findIndex(fileItem = > {
                    return fileItem.uid === file.uid
                }
            )
                fileList.splice(index, 1)
            },
            //是否产生费用
            costFlagChange: function (target) {
                if (target === 'no') {
                    this.costDisabledInput = true;
                } else {
                    this.costDisabledInput = false;
                }
            },
            //是否外聘律师
            outsideLawyerFlagChange: function (target) {
                if (target === 'yes') {
                    this.outsideLawyerYes = true;
                } else {
                    this.outsideLawyerYes = false;
                }
            }
        }
    }
</script>
