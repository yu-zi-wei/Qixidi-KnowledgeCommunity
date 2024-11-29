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
    this.$cookies.set(tokenName, token, {
      path: '/',
      maxAge: 60 * 60 * 24 * 7, // 7 days
      sameSite: 'none',
      secure: true
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
