import {encode, decode} from 'base-64';
import Vue from 'vue';

const modal = {
  //base-64 加密
  encode(data) {
    if (data==null){
      return '';
    }
    const encodedString = encode(data);
    return encodedString;
  },
  //base-64 解密
  decode(data) {
    if (data==null){
      return '';
    }
    const decodedString = decode(data);
    return decodedString;
  }
}
Vue.prototype.$base64 = modal;
