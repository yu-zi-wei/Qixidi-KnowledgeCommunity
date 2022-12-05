export function downloadByBlob(url, name) {
    //第一个参数是图片链接，第二个参数是图片名称
    let image = new Image();
    image.setAttribute('crossOrigin', 'anonymous');
    image.src = url;
    image.onload = () => {
        let canvas = document.createElement('canvas');
        canvas.width = image.width;
        canvas.height = image.height;
        let ctx = canvas.getContext('2d');
        ctx.drawImage(image, 0, 0, image.width, image.height);
        canvas.toBlob((blob) => {
            let url = URL.createObjectURL(blob);
            this.download(url, name);
            // 用完释放URL对象
            URL.revokeObjectURL(url);
        });
    };
};

function download(href, name) {
    let eleLink = document.createElement('a');
    eleLink.download = name;
    eleLink.href = href;
    eleLink.click();
    eleLink.remove();
};