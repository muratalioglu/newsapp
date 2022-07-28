import { useState } from "react";

const CreateNews = () => {

    const [news, setNews] = useState({
        title: "",
        content: "",
        imageUrl: ""
    })

    const handleTitleInputChange = (e) => {        
        setNews(            
            (news) => ({
                ...news,
                title: e.target.value
            })
        )
    };

    const handleContentInputChange = (e) => {
        setNews(
            (news) => ({
                ...news,
                content: e.target.value
            })
        )
    };

    const handleImageUrlInputChange = (e) => {
        setNews(
            (news) => ({
                ...news,
                imageUrl: e.target.value
            })
        )
    };

    const sendNewsForm = (e) => {

        e.preventDefault();

        fetch(`http://localhost:8080/news`, 
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    "title": news.title,
                    "content": news.content,
                    "imageUrl": news.imageUrl
                })
            }
        )
    }

    return (
        <div>
            <h3>Create News</h3>
            <form onSubmit={sendNewsForm}>
                <table>
                    <tbody>
                        <tr>
                            <td><label>Title</label></td>
                        </tr>
                        <tr>
                            <td><input type="text" name="title" onChange={handleTitleInputChange} /></td>
                        </tr>
                        <tr>
                            <td><label>Content</label></td>
                        </tr>
                        <tr>
                            <td><textarea rows="10" cols="50" name="content" value={news.content} onChange={handleContentInputChange} /></td>
                        </tr>
                        <tr>
                            <td><label>Görsel</label></td>
                        </tr>
                        <tr>
                            <td><input type="file" name="filename" onChange={handleImageUrlInputChange} /></td>
                        </tr>
                        <tr>
                            <td><button type="submit">Publish</button></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    )
};

export default CreateNews;