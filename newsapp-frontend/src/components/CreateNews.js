const CreateNews = () => {

    return (
        <div>
            <h3>Create News</h3>
            <form>
                <table>
                    <tbody>
                        <tr>
                            <td><label>Title</label></td>
                        </tr>
                        <tr>
                            <td><input type="text" name="title" /></td>
                        </tr>
                        <tr>
                            <td><label>Content</label></td>
                        </tr>
                        <tr>
                            <td><textarea rows="10" cols="50" name="content" /></td>
                        </tr>
                        <tr>
                            <td><label>Görsel</label></td>
                        </tr>
                        <tr>
                            <td><input type="file" name="filename" /></td>
                        </tr>
                        <tr>
                            <td><button>Publish</button></td>
                        </tr>
                    </tbody>
                </table>
            </form>
        </div>
    )
};

export default CreateNews;