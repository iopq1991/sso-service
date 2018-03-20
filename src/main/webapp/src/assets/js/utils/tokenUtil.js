import $x from '../$x.js';

export default {
    get () {
        return $x.storageUtil.getSessionObj($x.CONST.TOKEN_HEADER);
    },
    set (val) {
        $x.storageUtil.setSessionObj($x.CONST.TOKEN_HEADER, val);
    },
    remove () {
        this.set(undefined);
    }
}