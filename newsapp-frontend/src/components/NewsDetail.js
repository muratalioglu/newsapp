import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const NewsDetail = () => {    

    const [news, setNews] = useState({"title": null, "content": null, "imageUrl": null});

    let { newsId } = useParams();

    useEffect(() => {
        
        fetch(`http://localhost:8080/news/${newsId}`)
            .then(res => res.json())
            .then(
                res => 
                setNews(
                    {
                        "title": res.title,
                        "content": res.content,
                        "imageUrl": res.imageUrl
                    }
                )
            )
    }, [newsId])

    return (
        <div>
            <h3>{news.title}</h3>
            <p>{news.content}</p>
            <img src={news.imageUrl} alt={news.title} />
        </div>
    )
};

export default NewsDetail;