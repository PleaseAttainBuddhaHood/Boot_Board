// 파일 업로드
async function uploadToServer(formObj)
{
    console.log("서버로 업로드");
    console.log("formObj : " + formObj);

    const response = await axios(
    {
        method: 'post',
        url: '/upload',
        data: formObj,
        headers:
        {
            'Content-Type': 'multipart/form-data'
        }
    });

    return response.data;
}

// 파일 삭제
async function removeFileToServer(uuid, fileName)
{
    const response = await axios.delete(`/remove/${uuid}_${fileName}`);

    return response.data;
}
