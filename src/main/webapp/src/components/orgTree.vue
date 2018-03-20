<template>
    <el-tree
            ref="tree"
            :data="treeData"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :expand-on-click-node="false"
            empty-text="[空]"
            highlight-current
            lazy
            :load="loadSubNodes"
            :current-node-key="currentNode"
            @check-change="handleCheckChange"
            node-key="id"
            :default-expanded-keys="treeData.map(i => i.id)"
            class="org-tree-rt"
    >
    </el-tree>
</template>

<script>
    import Vue from 'vue';
    import {Tree, Checkbox} from 'element-ui';

    Vue.use(Tree).use(Checkbox);

    export default {
        props: {
            //currId: String //当前节点id。如果有指定，则树初始化的时候，需要从根节点依次加载到此节点
        },
        data() {
            return {
                treeData: [],
                currentNode: '',
                //expandNode: [this.currId], //展开的节点
                defaultProps: {
                    children: 'childNodes',
                    label: 'name',
                    disabled: '!canClick',
                    isLeaf: 'leaf',
                },
            };
        },
        mounted: function () {
            this.loadTreeData();
        },
        methods: {
            loadTreeData() {
                var self = this;
                this.$x.get('/Organize/Root', {}, {showError: 'toast'})
                    .then(res => {
                        res.data = res.data || [];
                        res.data.forEach(function (v) {
                            if (v['hasChild'] == 0) {
                                v['leaf'] = true;
                            }
                        });
                        this.treeData = res.data;
                        self.currentNode = res.data && res.data.length && res.data[0]['id'];

                        this.$emit('root-loaded', res.data[0]);  //根节点已加载
                    });
            },
            loadSubNodes(node, resolve) {
                if (node.level === 0) {
                    return resolve([]);
                }
                if (node.data.memberFlag) {
                    return resolve([]);
                }
                if (node.data.children) {
                    resolve(node.data.children)
                }
                else {
                    this.$x.post('/Organize/Child', {id: node.data.id}, {showError: 'toast', maskOptions: false})
                        .then(res => {
                            var childData = [];
                            //res.data.memberList=res.data.memberList||[];
                            res.data.orgList = res.data.orgList || [];
                            res.data.orgList.forEach(function (v) {
                                if (v['hasChild'] == 0) {
                                    v['leaf'] = true;
                                }
                            });
                            childData.push(...res.data.orgList);
                            resolve(childData);
                        });
                }
            },

            handleNodeClick(data) {
                this.$emit('node-click', data);
            },
            handleCheckChange() {

            },
            getCheckedKeys() {
                return this.$refs.tree.getCheckedKeys();
            }
        },
        computed: {},
        components: {}
    }
</script>

<style scoped lang="scss">
    .org-tree-rt {
        min-width: 100%;
    }
</style>
