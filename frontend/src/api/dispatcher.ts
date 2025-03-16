import request from '@/utils/request';

/**
 * 调度员查询参数
 */
export interface DispatcherQuery {
  name?: string;
  organizationId?: string;
  status?: string;
  pageNum?: number;
  pageSize?: number;
}

/**
 * 调度员表单数据
 */
export interface DispatcherForm {
  id?: string;
  name: string;
  code: string;
  organizationId: string;
  userId?: string;
  status?: string;
}

/**
 * 调度员信息
 */
export interface DispatcherInfo {
  id: string;
  name: string;
  code: string;
  organizationId: string;
  userId?: string;
  status: string;
  createTime: string;
  updateTime: string;
}

/**
 * 获取调度员分页列表
 * @param params 查询参数
 * @returns 分页数据
 */
export function getDispatcherList(params: DispatcherQuery) {
  return request({
    url: '/api/dispatchers/page',
    method: 'get',
    params: {
      page: params.pageNum ? params.pageNum - 1 : 0,
      size: params.pageSize || 10,
      name: params.name,
      organizationId: params.organizationId,
      status: params.status
    }
  });
}

/**
 * 获取所有调度员
 * @returns 调度员列表
 */
export function getAllDispatchers() {
  return request({
    url: '/api/dispatchers',
    method: 'get'
  });
}

/**
 * 根据组织ID获取调度员列表
 * @param organizationId 组织ID
 * @returns 调度员列表
 */
export function getDispatchersByOrganizationId(organizationId: string) {
  return request({
    url: `/api/dispatchers/organization/${organizationId}`,
    method: 'get'
  });
}

/**
 * 根据用户ID获取调度员
 * @param userId 用户ID
 * @returns 调度员信息
 */
export function getDispatcherByUserId(userId: string) {
  return request({
    url: `/api/dispatchers/user/${userId}`,
    method: 'get'
  });
}

/**
 * 根据状态获取调度员列表
 * @param status 状态
 * @returns 调度员列表
 */
export function getDispatchersByStatus(status: string) {
  return request({
    url: `/api/dispatchers/status/${status}`,
    method: 'get'
  });
}

/**
 * 获取调度员详情
 * @param id 调度员ID
 * @returns 调度员详情
 */
export function getDispatcherDetail(id: string) {
  return request({
    url: `/api/dispatchers/${id}`,
    method: 'get'
  });
}

/**
 * 创建调度员
 * @param data 调度员数据
 * @returns 创建结果
 */
export function createDispatcher(data: DispatcherForm) {
  return request({
    url: '/api/dispatchers',
    method: 'post',
    data
  });
}

/**
 * 更新调度员
 * @param id 调度员ID
 * @param data 调度员数据
 * @returns 更新结果
 */
export function updateDispatcher(id: string, data: DispatcherForm) {
  return request({
    url: `/api/dispatchers/${id}`,
    method: 'put',
    data
  });
}

/**
 * 删除调度员
 * @param id 调度员ID
 * @returns 删除结果
 */
export function deleteDispatcher(id: string) {
  return request({
    url: `/api/dispatchers/${id}`,
    method: 'delete'
  });
}

/**
 * 更新调度员状态
 * @param id 调度员ID
 * @param status 状态
 * @returns 更新结果
 */
export function updateDispatcherStatus(id: string, status: string) {
  return request({
    url: `/api/dispatchers/${id}/status`,
    method: 'put',
    params: { status }
  });
}

/**
 * 检查调度员编码是否存在
 * @param code 编码
 * @returns 是否存在
 */
export function checkDispatcherCode(code: string) {
  return request({
    url: '/api/dispatchers/check-code',
    method: 'get',
    params: { code }
  });
}

/**
 * 检查调度员编码是否存在（排除指定ID）
 * @param code 编码
 * @param excludeId 排除的ID
 * @returns 是否存在
 */
export function checkDispatcherCodeExcludeId(code: string, excludeId: string) {
  return request({
    url: '/api/dispatchers/check-code-exclude',
    method: 'get',
    params: { code, excludeId }
  });
}

/**
 * 为调度员分配子系统
 * @param dispatcherId 调度员ID
 * @param subsystemIds 子系统ID列表
 * @returns 分配结果
 */
export function assignSubsystems(dispatcherId: string, subsystemIds: string[]) {
  return request({
    url: `/api/dispatchers/${dispatcherId}/subsystems`,
    method: 'post',
    data: subsystemIds
  });
}

/**
 * 移除调度员的子系统分配
 * @param dispatcherId 调度员ID
 * @param subsystemId 子系统ID
 * @returns 移除结果
 */
export function removeSubsystem(dispatcherId: string, subsystemId: string) {
  return request({
    url: `/api/dispatchers/${dispatcherId}/subsystems/${subsystemId}`,
    method: 'delete'
  });
}

/**
 * 获取调度员已分配的子系统ID列表
 * @param dispatcherId 调度员ID
 * @returns 子系统ID列表
 */
export function getAssignedSubsystemIds(dispatcherId: string) {
  return request({
    url: `/api/dispatchers/${dispatcherId}/subsystems`,
    method: 'get'
  });
}

/**
 * 检查调度员是否已分配指定子系统
 * @param dispatcherId 调度员ID
 * @param subsystemId 子系统ID
 * @returns 是否已分配
 */
export function isSubsystemAssigned(dispatcherId: string, subsystemId: string) {
  return request({
    url: `/api/dispatchers/${dispatcherId}/subsystems/${subsystemId}/check`,
    method: 'get'
  });
}