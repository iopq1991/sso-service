<script>
    //import  from '';
    export default {
        props: {
            src: String, //图像url，如果为空则根据name来
            name: String //姓名
        },
        render(h) {
            if (this.src) {
                return h('img', {attrs: {src: this.src}, class: ['avatar', this.class], style: this.style})
            }
            else {
                return h('div', {class: ['avatar', 'fake-avatar', this.class], style: this.style}
                    , [h('span', {}, this.avatarText)]
                )
            }
        },
        computed: {
            //显示的文本
            avatarText() {
                if (this.name && this.name.length) {
                    if (this.name.match(/[\u4e00-\u9fa5]/)) {
                        //汉字，取第后两个
                        return this.name.slice(-2);
                    }
                    else {
                        //英文，取首字母
                        var name = this.name, names = name.split(' ');
                        if (names.length > 1)
                            return names.map(n => n.length ? n.slice(0, 1) : '').join('').toUpperCase();
                        else
                            return name.length > 4 ? (name.slice(0, 1) + name.slice(name.length - 1)).toUpperCase()
                                : name
                    }
                }
                return ''
            }
        }
    }
</script>

<style scoped lang="scss">
    @import "../assets/css/defines.scss";

    .avatar {
        width: .56rem;
        height: .56rem;
        font-size: .17rem;
        border-radius: 50%;
    }

    .fake-avatar {
        display: table-cell;
        text-align: center;
        vertical-align: middle;
        background: #4DA9EC;
        color: #fff;
    }
</style>
