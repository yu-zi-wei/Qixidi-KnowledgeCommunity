import request from '../uitls/request'

export function listSidebar(query) {
    return request({
        url: '/white/configure/sidebar/list',
        method: 'get',
        params: query
    })
}