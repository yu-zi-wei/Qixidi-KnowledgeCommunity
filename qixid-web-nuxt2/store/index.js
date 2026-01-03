export const actions = {
  nuxtServerInit(store, context) {
    // console.log(store, "nuxtServerInit")
  }
};
let tokenName = "aurora-token"

export const state = {
  token: '',
};
export const mutations = {
  setToken(state, token) {
    this.state.token = token;
    // 有效期，path: '/' 确保全站路径下都能读到
    this.$cookies.set(tokenName, token, {
      maxAge: 60 * 60 * 24 * 365,
      path: '/'
    });
  },
  getToken() {
    this.state.token = this.$cookies.get(tokenName)
    return this.state.token;
  },
  removeToken() {
    this.state.token = '';
    this.$cookies.remove(tokenName);
  },
}
