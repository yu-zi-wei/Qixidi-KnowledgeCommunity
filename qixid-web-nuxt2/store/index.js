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
    this.$cookies.set(tokenName, token);
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
