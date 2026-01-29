import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 30000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    const message = error.response?.data?.message || error.message || '请求失败'
    return Promise.reject(new Error(message))
  }
)

// 生成食谱
export const generateRecipesAPI = async (params) => {
  return await api.post('/recipes/generate', params)
}

// 获取食谱详情
export const getRecipeDetail = async (id) => {
  return await api.get(`/recipes/${id}`)
}

export default api
