import { useState } from "react";

import { Link, useLocation, useParams } from "react-router-dom";

const EditNews = () => {

    const { newsId } = useParams();

    const location = useLocation();
    const [news, setNews] = useState(location.state);    

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

        const formData = new FormData();
        formData.append("title", news.title)
        formData.append("content", news.content)
        formData.append("imageUrl", news.imageUrl)

        fetch(`http://localhost:8080/news/${newsId}`,
            {
                method: "PATCH",
                body: formData
            }
        )

        window.location.href = `/news/${newsId}`;
    };

    return (
        <div>
            <Link to="/">Home</Link>
            <h3>Edit News</h3>
            <form onSubmit={sendNewsForm}>
                <table>
                    <tbody>
                        <tr>
                            <td><label>Title</label></td>
                        </tr>
                        <tr>
                            <td><input type="text" name="title" value={news.title} onChange={handleTitleInputChange} /></td>
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

export default EditNews;