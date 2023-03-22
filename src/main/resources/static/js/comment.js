async function get1(bno)
{
    return await axios.get(`/comments/list/${bno}`);
}

async function getList({bno, page, size, goLast})
{
    const result = await axios.get(`/comments/list/${bno}`, {params: {page, size}});

    if(goLast)
    {
        const total = result.data.total;
        const lastPage = parseInt(Math.ceil(total / size));

        return getList({bno: bno, page: lastPage, size: size});
    }

    return result.data;
}

// 댓글 추가
async function addComment(commentObj)
{
    const response = await axios.post(`/comments/`, commentObj);

    return response.data;
}


// 댓글 조회
async function getComment(rno)
{
    const response = await axios.get(`/comments/${rno}`);

    return response.data;
}

// 댓글 수정
async function modifyComment(commentObj)
{
    const response = await axios.put(`/comments/${commentObj.rno}`, commentObj);

    return response.data;
}

// 댓글 삭제
async function removeComment(rno)
{
    const response = await axios.delete(`/comments/${rno}`);

    return response.data;
}