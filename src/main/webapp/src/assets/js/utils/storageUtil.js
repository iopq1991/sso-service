'use strict'

function getFrom (storage, name) {
    try {
        var str = storage.getItem(name);
        return str && JSON.parse(str);
    } catch (e) {
        console.warn(e)
    }
}

function setTo (storage, name, obj) {
    if (obj === undefined)
        storage.removeItem(name);
    else
        storage.setItem(name, JSON.stringify(obj));
}

export default {
    getObj (name) {
        return getFrom(localStorage, name)
    },
    setObj (name, obj) {
        setTo(localStorage, name, obj);
    },
    getSessionObj (name) {
        return getFrom(sessionStorage, name);
    },
    setSessionObj (name, obj) {
        setTo(sessionStorage, name, obj);
    },
}