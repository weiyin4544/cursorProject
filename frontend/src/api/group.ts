import request from '@/utils/request';

/**
 * 群组查询参数
 */
export interface GroupQuery {
  name?: string;
  organizationId?: string;
  type?: string;
  status?: string;
  pageNum?: number;
  pageSize?: number;
}

/**
 * 群组表单数据
 */
export interface GroupForm {
  id?: string;
  name: string;
  code: string;
  organizationId: string;
  type: string;
  description?: string;
  status?: string;
}

/**
 * 群组信息
 */
export interface GroupInfo {
  id: string;
  name: string;
  code: string;
  organizationId: string;
  type: string;
  description?: string;
  status: string;
  createTime: string;
  updateTime: string;
}

/**
 * 获取群组分页列表
 * @param params 查询参数
 * @returns 分页数据
 */
export function getGroupList(params: GroupQuery) {
  return request.get('/api/groups/page', {
    params: {
      page: params.pageNum ? params.pageNum - 1 : 0,
      size: params.pageSize || 10,
      name: params.name,
      organizationId: params.organizationId,
      type: params.type,
      status: params.status
    }
  });
}

/**
 * 获取所有群组
 * @returns 群组列表
 */
export function getAllGroups() {
  return request.get('/api/groups');
}

/**
 * 根据组织ID获取群组列表
 * @param organizationId 组织ID
 * @returns 群组列表
 */
export function getGroupsByOrganizationId(organizationId: string) {
  return request.get(`/api/groups/organization/${organizationId}`);
}

/**
 * 根据类型获取群组列表
 * @param type 群组类型
 * @returns 群组列表
 */
export function getGroupsByType(type: string) {
  return request.get(`/api/groups/type/${type}`);
}

/**
 * 根据状态获取群组列表
 * @param status 状态
 * @returns 群组列表
 */
export function getGroupsByStatus(status: string) {
  return request.get(`/api/groups/status/${status}`);
}

/**
 * 获取群组详情
 * @param id 群组ID
 * @returns 群组详情
 */
export function getGroupDetail(id: string) {
  return request.get(`/api/groups/${id}`);
}

/**
 * 创建群组
 * @param data 群组数据
 * @returns 创建结果
 */
export function createGroup(data: GroupForm) {
  return request.post('/api/groups', data);
}

/**
 * 更新群组
 * @param id 群组ID
 * @param data 群组数据
 * @returns 更新结果
 */
export function updateGroup(id: string, data: GroupForm) {
  return request.put(`/api/groups/${id}`, data);
}

/**
 * 删除群组
 * @param id 群组ID
 * @returns 删除结果
 */
export function deleteGroup(id: string) {
  return request.delete(`/api/groups/${id}`);
}

/**
 * 更新群组状态
 * @param id 群组ID
 * @param status 状态
 * @returns 更新结果
 */
export function updateGroupStatus(id: string, status: string) {
  return request.put(`/api/groups/${id}/status`, null, {
    params: { status }
  });
}

/**
 * 检查群组编码是否存在
 * @param code 编码
 * @returns 是否存在
 */
export function checkGroupCode(code: string) {
  return request.get('/api/groups/check-code', {
    params: { code }
  });
}

/**
 * 检查群组编码是否存在（排除指定ID）
 * @param code 编码
 * @param excludeId 排除的ID
 * @returns 是否存在
 */
export function checkGroupCodeExcludeId(code: string, excludeId: string) {
  return request.get('/api/groups/check-code-exclude', {
    params: { code, excludeId }
  });
}

/**
 * 添加原生组到派接组
 * @param patchGroupId 派接组ID
 * @param nativeGroupId 原生组ID
 * @returns 关联信息
 */
export function addNativeGroupToPatchGroup(patchGroupId: string, nativeGroupId: string) {
  return request.post(`/api/groups/patch/${patchGroupId}/native/${nativeGroupId}`);
}

/**
 * 从派接组移除原生组
 * @param patchGroupId 派接组ID
 * @param nativeGroupId 原生组ID
 * @returns 移除结果
 */
export function removeNativeGroupFromPatchGroup(patchGroupId: string, nativeGroupId: string) {
  return request.delete(`/api/groups/patch/${patchGroupId}/native/${nativeGroupId}`);
}

/**
 * 获取派接组下的原生组列表
 * @param patchGroupId 派接组ID
 * @returns 原生组列表
 */
export function getNativeGroupsByPatchGroupId(patchGroupId: string) {
  return request.get(`/api/groups/patch/${patchGroupId}/natives`);
}

/**
 * 获取原生组所属的派接组列表
 * @param nativeGroupId 原生组ID
 * @returns 派接组列表
 */
export function getPatchGroupsByNativeGroupId(nativeGroupId: string) {
  return request.get(`/api/groups/native/${nativeGroupId}/patches`);
}

/**
 * 添加终端到原生组
 * @param nativeGroupId 原生组ID
 * @param terminalId 终端ID
 * @returns 关联信息
 */
export function addTerminalToNativeGroup(nativeGroupId: string, terminalId: string) {
  return request.post(`/api/groups/native/${nativeGroupId}/terminal/${terminalId}`);
}

/**
 * 从原生组移除终端
 * @param nativeGroupId 原生组ID
 * @param terminalId 终端ID
 * @returns 移除结果
 */
export function removeTerminalFromNativeGroup(nativeGroupId: string, terminalId: string) {
  return request.delete(`/api/groups/native/${nativeGroupId}/terminal/${terminalId}`);
}

/**
 * 获取原生组下的终端列表
 * @param nativeGroupId 原生组ID
 * @returns 终端列表
 */
export function getTerminalsByNativeGroupId(nativeGroupId: string) {
  return request.get(`/api/groups/native/${nativeGroupId}/terminals`);
}

/**
 * 获取终端所属的原生组列表
 * @param terminalId 终端ID
 * @returns 原生组列表
 */
export function getNativeGroupsByTerminalId(terminalId: string) {
  return request.get(`/api/groups/terminal/${terminalId}/natives`);
}

/**
 * 添加调度员到本地组
 * @param localGroupId 本地组ID
 * @param dispatcherId 调度员ID
 * @returns 关联信息
 */
export function addDispatcherToLocalGroup(localGroupId: string, dispatcherId: string) {
  return request.post(`/api/groups/local/${localGroupId}/dispatcher/${dispatcherId}`);
}

/**
 * 从本地组移除调度员
 * @param localGroupId 本地组ID
 * @param dispatcherId 调度员ID
 * @returns 移除结果
 */
export function removeDispatcherFromLocalGroup(localGroupId: string, dispatcherId: string) {
  return request.delete(`/api/groups/local/${localGroupId}/dispatcher/${dispatcherId}`);
}

/**
 * 获取本地组下的调度员列表
 * @param localGroupId 本地组ID
 * @returns 调度员列表
 */
export function getDispatchersByLocalGroupId(localGroupId: string) {
  return request.get(`/api/groups/local/${localGroupId}/dispatchers`);
}

/**
 * 获取调度员所属的本地组列表
 * @param dispatcherId 调度员ID
 * @returns 本地组列表
 */
export function getLocalGroupsByDispatcherId(dispatcherId: string) {
  return request.get(`/api/groups/dispatcher/${dispatcherId}/locals`);
}

/**
 * 批量添加终端到原生组
 * @param nativeGroupId 原生组ID
 * @param terminalIds 终端ID列表
 * @returns 添加结果
 */
export function batchAddTerminalsToNativeGroup(nativeGroupId: string, terminalIds: string[]) {
  return request.post(`/api/groups/native/${nativeGroupId}/terminals`, terminalIds);
}

/**
 * 批量添加原生组到派接组
 * @param patchGroupId 派接组ID
 * @param nativeGroupIds 原生组ID列表
 * @returns 添加结果
 */
export function batchAddNativeGroupsToPatchGroup(patchGroupId: string, nativeGroupIds: string[]) {
  return request.post(`/api/groups/patch/${patchGroupId}/natives`, nativeGroupIds);
}

/**
 * 批量添加调度员到本地组
 * @param localGroupId 本地组ID
 * @param dispatcherIds 调度员ID列表
 * @returns 添加结果
 */
export function batchAddDispatchersToLocalGroup(localGroupId: string, dispatcherIds: string[]) {
  return request.post(`/api/groups/local/${localGroupId}/dispatchers`, dispatcherIds);
} 