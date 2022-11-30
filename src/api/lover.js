import request from '../uitls/request'

// 查询网站基本信息列表
export function listInfo(query) {
    return request({
        url: '/api/lover/basicInfo',
        method: 'get',
        params: query
    })
}

// 时光相册列表
export function listAlbum(query) {
    return request({
        url: '/api/lover/album',
        method: 'get',
        params: query
    })
}


// 查询爱情记录列表
export function listRecord(query) {
    return request({
        url: '/api/lover/loverRecord',
        method: 'get',
        params: query
    })
}

// 查询爱情清单列表
export function listRepertoire(query) {
    return request({
        url: '/api/lover/repertoire',
        method: 'get',
        params: query
    })
}