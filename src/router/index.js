import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'index',
        meta: {keepAlive: false},
        component: () => import('@/views/index'),
        children: [
            {
                path: '/',
                name: 'admin',
                meta: {keepAlive: false},
                component: () => import('@/views/admin')
            },
            {
                path: '/blessing',
                name: 'blessing',
                meta: {keepAlive: false},
                component: () => import('@/views/blessing')
            },
            {
                path: '/about',
                name: 'about',
                meta: {keepAlive: false},
                component: () => import('@/views/about')
            },
            {
                path: '/album',
                name: 'album',
                meta: {keepAlive: false},
                component: () => import('@/views/album')
            },
            {
                path: '/detailed-list',
                name: 'detailed-list',
                meta: {keepAlive: false},
                component: () => import('@/views/detailed-list')
            },
            {
                path: '/record',
                name: 'record',
                meta: {keepAlive: false},
                component: () => import('@/views/record')
            },
            {
                path: '/record-index',
                name: 'record-index',
                meta: {keepAlive: false},
                component: () => import('@/views/record-index')
            },
        ],
    },
    {
        path: '/lover-tree',
        name: 'lover-tree',
        meta: {keepAlive: false},
        component: () => import('@/views/lover-tree')
    },
    // {
    //     path: '/blessing',
    //     name: 'blessing',
    //     meta: {keepAlive: false},
    //     component: () => import('@/views/blessing')
    // },

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
