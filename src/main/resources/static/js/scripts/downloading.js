//https://stackoverflow.com/questions/35038884/download-file-from-bytes-in-javascript
function base64ToArrayBuffer(base64) {
    var binaryString = window.atob(base64);
    var binaryLen = binaryString.length;
    var bytes = new Uint8Array(binaryLen);
    for (var i = 0; i < binaryLen; i++) {
        var ascii = binaryString.charCodeAt(i);
        bytes[i] = ascii;
    }
    return bytes;
}
//https://dev.to/nombrekeff/download-file-from-blob-21ho
function downloadBlob(blob, name = 'file.txt') {
    if (
        window.navigator &&
        window.navigator.msSaveOrOpenBlob
    ) return window.navigator.msSaveOrOpenBlob(blob);

    // For other browsers:
    // Create a link pointing to the ObjectURL containing the blob.
    const data = window.URL.createObjectURL(blob);

    const link = document.createElement('a');
    link.href = data;
    link.download = name;

    // this is necessary as link.click() does not work on the latest firefox
    link.dispatchEvent(
        new MouseEvent('click', {
            bubbles: true,
            cancelable: true,
            view: window
        })
    );

    setTimeout(() => {
        // For Firefox it is necessary to delay revoking the ObjectURL
        window.URL.revokeObjectURL(data);
        link.remove();
    }, 100);
}

function base64ToHex(str) {
    const raw = atob(str);
    let result = '';
    for (let i = 0; i < raw.length; i++) {
        const hex = raw.charCodeAt(i).toString(16);
        result += (hex.length === 2 ? hex : '0' + hex);
    }
    return result.toUpperCase();
}
$(".download-file").click(function () {

    let filename = $(this).attr("data-filename");

    $.get("/download/"+filename, function(downloadData, status){

        // shows what happens if the signature is changed
        if(filename==="test.txt"){
            downloadData.digitalSignature += "a"+downloadData.digitalSignature.substring(1);
        }

        let fileBuffer = base64ToArrayBuffer(downloadData.file);

        $.ajax({
            method: "POST",
            url: "/validate/"+filename,
            data: {
                digitalSignatureHex: downloadData.digitalSignature,
                fileReceivedHex: base64ToHex(downloadData.file)
            }
        }).done(function( validationResults ) {
            if(validationResults.success){
                downloadBlob(new Blob([fileBuffer]),filename);
            }else{
                $(".download-file[data-filename='"+filename+"']").addClass("bad");
            }
        });
    });
});