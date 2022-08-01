export function handleTime(time) {
    time = Math.floor(time);
    let m = Math.floor(time / 60);
    let s = Math.floor(time % 60);
    m = m < 10 ? '0' + m : m;
    s = s < 10 ? '0' + s : s;
    return m + ':' + s;
}

// 处理超过n位的字符串
export function handleLongString(str, fs = 10, bs = 10, n = 30) {
    if (str.length < n) {
        return str;
    }
    let front = str.slice(0, fs);
    let behind = str.slice(-bs);
    return front + '...' + behind;
}

// 处理相册时间显示
export function handleAlbumDateShow(date) {
    let myDate = new Date();
    let currentYear = myDate.getFullYear()
    let currentMonth = myDate.getMonth() + 1
    let currentDay = myDate.getDate()

    let arr = date.split('-')
    arr[0] = parseInt(arr[0])
    arr[1] = parseInt(arr[1])
    arr[2] = parseInt(arr[2])

    // 判断是否是今天
    if (arr[0] == currentYear && arr[1] == currentMonth && arr[2] == currentDay) {
        return '今天'
    }

    if (arr[0] == currentYear) {
        return arr[1] + '月' + arr[2] + '日'
    } else {
        return arr[0] + '年' + arr[1] + '月' + arr[2] + '日'
    }
}

// 判断文件类型并返回对应图标img名称
export function getTypeIcon(type) {
    // word
    if (type == "doc" || type == "docx") {
        return 'word'
    }
    // excel
    else if (type == "xlsx" || type == "xls") {
        return 'excel'
    }
    // txt
    else if (type == "txt") {
        return 'txt'
    }
    // pdf
    else if (type == "pdf") {
        return 'pdf'
    }
    // zip
    else if (
        type == "zip" ||
        type == "7z" ||
        type == "rar" ||
        type == "jar"
    ) {
        return 'zip'
    }
    // ppt
    else if (type == "ppt" || type == "pptx") {
        return 'ppt'
    }
    // unknown
    else {
        return 'unknown'
    }
}