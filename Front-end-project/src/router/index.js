import Vue from 'vue'
import VueRouter from 'vue-router'
import vuexIndex from '@/store/index.js'


const Index = () => import('views/index.vue')
const Files = () => import('views/files/Files.vue')
const Albums = () => import('views/albums/Albums.vue')
const Collectes = () => import('views/collectes/Collectes.vue')
const Login = () => import('views/login/Login.vue')


const routes = [
  { path: '/', redirect: '/index' },
  {
    path: '/index',
    component: Index,
    redirect: '/files/%2Froot',
    children: [
      { path: '/files', redirect: '/files/%2Froot' },
      { path: '/files/:path', name: 'files', component: Files },
      { path: '/albums', component: Albums },
      { path: '/collectes', component: Collectes },
    ]
  },
  { path: '/login', component: Login },
]

Vue.use(VueRouter)



const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path != '/login' && !vuexIndex.state.userInfo) {
    router.replace('/login')
  }
  next()
})

export default router
