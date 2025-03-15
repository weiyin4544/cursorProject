import request from '@/utils/request'

// 组织查询参数类型
export interface OrgQuery {
  page?: number
  limit?: number
  name?: string
  code?: string
  status?: string
}

// 组织表单类型
export interface OrgForm {
  id?: string
  name: string
  code: string
  description?: string
  parentId?: string
  status: string
}

// 组织信息类型
export interface OrgInfo extends OrgForm {
  id: string
  createTime: string
  updateTime: string
  children?: OrgInfo[]
  hasChildren?: boolean
}

// 获取组织列表
export function getOrgList(params: OrgQuery) {
  return request.get<{
    data: OrgInfo[]
    total: number
  }>('/api/organizations', params)
}

// 获取组织树
export function getOrgTree() {
  return request.get<OrgInfo[]>('/api/organizations/tree')
}

// 获取组织详情
export function getOrgDetail(id: string) {
  return request.get<OrgInfo>(`/api/organizations/${id}`)
}

// 创建组织
export function createOrg(data: OrgForm) {
  return request.post<void>('/api/organizations', data)
}

// 更新组织
export function updateOrg(id: string, data: OrgForm) {
  return request.put<void>(`/api/organizations/${id}`, data)
}

// 删除组织
export function deleteOrg(id: string) {
  return request.delete<void>(`/api/organizations/${id}`)
}

// 检查组织编码是否存在
export function checkOrgCode(code: string) {
  return request.get<boolean>('/api/organizations/check-code', { code })
} 