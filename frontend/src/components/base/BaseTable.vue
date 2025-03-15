<template>
  <div class="base-table">
    <div class="table-header" v-if="$slots.header">
      <slot name="header"></slot>
    </div>
    <el-table
      v-loading="loading"
      :data="data"
      :border="border"
      :stripe="stripe"
      :height="height"
      :max-height="maxHeight"
      :row-key="rowKey"
      :tree-props="treeProps"
      :default-expand-all="defaultExpandAll"
      :highlight-current-row="highlightCurrentRow"
      @selection-change="handleSelectionChange"
      @current-change="handleCurrentChange"
      @row-click="handleRowClick"
    >
      <el-table-column
        v-if="selection"
        type="selection"
        width="55"
        align="center"
      />
      <el-table-column
        v-if="index"
        type="index"
        width="55"
        align="center"
        label="序号"
      />
      <slot></slot>
      <el-table-column
        v-if="$slots.operation"
        label="操作"
        :width="operationWidth"
        :fixed="operationFixed"
        align="center"
      >
        <template #default="scope">
          <slot name="operation" :row="scope.row" :index="scope.$index"></slot>
        </template>
      </el-table-column>
    </el-table>
    <div class="table-footer" v-if="pagination">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="pageSizes"
        :total="total"
        :background="true"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'

interface Props {
  data: any[]
  loading?: boolean
  border?: boolean
  stripe?: boolean
  height?: string | number
  maxHeight?: string | number
  rowKey?: string
  treeProps?: {
    children?: string
    hasChildren?: string
  }
  defaultExpandAll?: boolean
  highlightCurrentRow?: boolean
  selection?: boolean
  index?: boolean
  pagination?: boolean
  total?: number
  pageSize?: number
  pageSizes?: number[]
  operationWidth?: string | number
  operationFixed?: boolean | 'right' | 'left'
}

const props = withDefaults(defineProps<Props>(), {
  loading: false,
  border: true,
  stripe: true,
  rowKey: 'id',
  treeProps: () => ({ children: 'children', hasChildren: 'hasChildren' }),
  defaultExpandAll: false,
  highlightCurrentRow: true,
  selection: false,
  index: false,
  pagination: false,
  total: 0,
  pageSize: 10,
  pageSizes: () => [10, 20, 50, 100],
  operationWidth: 150,
  operationFixed: 'right'
})

const emit = defineEmits<{
  (e: 'selection-change', selection: any[]): void
  (e: 'current-change', currentRow: any, oldCurrentRow: any): void
  (e: 'row-click', row: any, column: any, event: Event): void
  (e: 'size-change', size: number): void
  (e: 'page-change', page: number): void
  (e: 'update:pageSize', size: number): void
  (e: 'update:currentPage', page: number): void
}>()

const currentPage = ref(1)
const pageSize = ref(props.pageSize)

watch(() => props.pageSize, (val) => {
  pageSize.value = val
})

const handleSelectionChange = (selection: any[]) => {
  emit('selection-change', selection)
}

const handleCurrentChange = (currentRow: any, oldCurrentRow: any) => {
  emit('current-change', currentRow, oldCurrentRow)
}

const handleRowClick = (row: any, column: any, event: Event) => {
  emit('row-click', row, column, event)
}

const handleSizeChange = (size: number) => {
  emit('size-change', size)
  emit('update:pageSize', size)
}

const handlePageChange = (page: number) => {
  emit('page-change', page)
  emit('update:currentPage', page)
}
</script>

<style scoped lang="scss">
.base-table {
  .table-header {
    margin-bottom: 16px;
  }

  .table-footer {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
</style> 