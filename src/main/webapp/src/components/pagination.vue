<template>
    <div class="pagination">
        <el-pagination
                layout="total, prev, pager, next,sizes"
                :total="total"
                :page-size="pageSize"
                :page-sizes="pageSizes"
                :current-page="currentPage"
                @current-change="handleCurrentChange"
                @size-change="handleSizeChange"
        >
        </el-pagination>
    </div>
</template>

<script>
    import {Pagination} from 'element-ui';

    Vue.use(Pagination);
    export default {
        data() {
            return {
                currentPage: 1
            }
        },
        props: {
            total: {
                type: Number
            },
            pageSize: {
                type: Number
            },
            pageSizes: {
                type: Array
            }
        },
        methods: {
            handleCurrentChange(val) {
                this.currentPage = val;
                this.$emit('currentChange', (val - 1) * this.pageSize); //返回当前页差值
                this.$emit('current-change', val); //返回当前页
            },
            handleSizeChange(val) {
                this.$emit('sizeChange', val);
            }
        },

    }
</script>

<style>
    .pagination {
        margin-top: 20px;
        text-align: right;
    }
</style>
