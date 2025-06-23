// 通知弹窗
import {createDiscreteApi} from 'naive-ui';

const {notification} = createDiscreteApi(['notification']);
type NotificationType = keyof typeof notification;
export const tips = (type: NotificationType, msg: string) => {
    notification[type]({
        content: msg,
        duration: 2500,
        keepAliveOnHover: true
    });
};

// 获取主题class名
import {inject} from 'vue';

export const useTheme = () => {
    return inject('theme') as Ref<any>;
};

/**
 * @description: 主题class名
 * @param {string} className class名不带-dark
 * @return {*}
 **/
export const themeClass = (className: string) => {
    const theme = useTheme();
    return theme.value ? className : `${className}-dark`;
};
