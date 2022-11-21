import {createRouter, createWebHistory} from 'vue-router'

const routes = [
    {
        path: '/',
        name: 'index',
        component: () => import('@/views/index'),
        children: [
            {
                path: '/',
                name: 'admin',
                component: () => import('@/views/admin')
            },
            {
                path: '/blessing',
                name: 'blessing',
                component: () => import('@/views/blessing')
            },
            {
                path: '/about',
                name: 'about',
                component: () => import('@/views/about')
            },
            {
                path: '/album',
                name: 'album',
                component: () => import('@/views/album')
            },
            {
                path: '/detailed-list',
                name: 'detailed-list',
                component: () => import('@/views/detailed-list')
            },
            {
                path: '/record',
                name: 'record',
                component: () => import('@/views/record')
            },
        ],
    },
    {
        path: '/lover-tree',
        name: 'lover-tree',
        component: () => import('@/views/lover-tree')
    },
    // {
    //     path: '/blessing',
    //     name: 'blessing',
    //     component: () => import('@/views/blessing')
    // },

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
