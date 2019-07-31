function pmd(arg) {
    var obj = {};
    obj['tool'] = 'PMD';
    var lines = arg.split('\n');
    if (lines[0].indexOf('net.sourceforge.pmd.PMD processFiles') !== -1 || lines.length > 0) {
        
        for (var i=1; i< lines.length; i++) {
            var indexes = colonIndexesFirst(lines[i]);
            if (indexes != null) {
                var lineNumber = lines[i].substring(indexes[0]+1,indexes[1]);
                lineNumber = parseInt(lineNumber);
                if (lineNumber !== NaN) {
                    var comment = lines[i].substring(indexes[1]+1);
                    var file = lines[i].substring(0, indexes[0]);
                    obj[file] = (typeof obj[file] != 'undefined' && obj[file] instanceof Array) ? obj[file] : [] 
                    obj[file].push({
                        'line' : lineNumber,
                        'comment' : comment
                    });
                }
            }
        }
    }
    return obj;
}

function colonIndexesFirst(str) {
    // windows specific nuance
    var indexes = [];
    for (var i = 0; i < str.length; i++) {
        if (str[i] === ':')
            indexes.push(i);
    }

    var length = indexes.length;
    if (length >= 3) return [indexes[1], indexes[2]];
    return null;
}

function checkstyle(arg) {
    var obj = {};
    obj['tool'] = 'Checkstyle';
    var lines = arg.split('\n');
    if (lines.length > 0 && lines[0].indexOf('Starting audit') !== -1) {
        
        for (var i=1; i< lines.length; i++) {
            var indexes = colonIndexesFirst(lines[i]);
            if (indexes != null) {
                var lineNumber = parseInt(lines[i].substring(indexes[0]+1,indexes[1]));
                if (lineNumber !== NaN) {
                    var comment = lines[i].substring(indexes[1]+1);
                    var file = lines[i].substring(0, indexes[0]);
                    var lineSplit = file.split(" ");
                    lineSplit.shift();
                    file = lineSplit.join(" ");
                    obj[file] = (typeof obj[file] != 'undefined' && obj[file] instanceof Array) ? obj[file] : [] 
                    obj[file].push({
                        'line' : lineNumber,
                        'comment' : comment
                    });
                }
            }
        }
    }
    return obj;
}