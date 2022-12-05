import request from '../utils/request'

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

// 查询爱情记录列表
export function listRecordInfo(id) {
    return request({
        url: '/api/lover/loverRecord/' + id,
        method: 'get',
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

// 查询爱情祝福语列表
export function listComment(query) {
    return request({
        url: '/api/lover/comment',
        method: 'get',
        params: query
    })
}

// 新增爱情祝福语
export function commentAdd(data) {
    return request({
        url: '/api/lover/insert/comment',
        method: 'post',
        data: data
    })
}

// 查询爱情树列表
export function listLoverTree() {
    return request({
        url: '/api/lover/loverTree',
        method: 'get',
    })
}

// 查询关于我们列表
export function listAbout() {
    return request({
        url: '/api/lover/about',
        method: 'get',
    })
}

// 查询爱情轮播图列表
export function listCarousel(query) {
    return request({
        url: '/api/lover/carousel',
        method: 'get',
        params: query
    })
}
