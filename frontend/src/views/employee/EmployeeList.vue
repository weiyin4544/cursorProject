  <el-table-column fixed="right" label="操作" width="200">
    <template #default="scope">
      <el-button link type="primary" size="small" @click="handleView(scope.row)">查看</el-button>
      <el-button link type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
      <el-button link type="primary" size="small" @click="handlePhones(scope.row)">电话管理</el-button>
      <el-button
        link
        :type="scope.row.status === 'ACTIVE' ? 'danger' : 'success'"
        size="small"
        @click="handleStatusChange(scope.row)"
      >
        {{ scope.row.status === 'ACTIVE' ? '设为离职' : '设为在职' }}
      </el-button>
      <el-button link type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
    </template>
  </el-table-column>

// 新增员工
const handleAdd = () => {
  router.push('/employee/add')
}

// 查看员工详情
const handleView = (row: any) => {
  router.push(`/employee/detail/${row.id}`)
}

// 编辑员工
const handleEdit = (row: any) => {
  router.push(`/employee/edit/${row.id}`)
}