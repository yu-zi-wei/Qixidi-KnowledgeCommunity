import request from '../uitls/request'


// export default {
//     listSidebar(query) {
//         return request({
//             url: '/white/configure/sidebar/list',
//             method: 'get',
//             params: query
//         })
//     },
//     listNavigation(query) {
//         return request({
//             url: '/white/configure/navigation/list',
//             method: 'get',
//             params: query
//         })
//     },
// }

export function listSidebar(query) {
    return request({
        url: '/white/configure/sidebar/list',
        method: 'get',
        params: query
    })
}