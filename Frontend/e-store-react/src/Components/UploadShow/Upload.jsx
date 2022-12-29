import React, {useState} from 'react';
import "./Upload.css";

const Upload = () => {
    const [data, setData] = useState(
        {
            id: 1,
            name: "",
            rating: 0,
            description: "",
            time: "",
            type: "",
            directorName: "",
            showCasts: [],
            showGenre: [],
            images: []
        });
    let [fileNames, setFilesNames] = useState({
        name1: "",
        name2: "",
        name3: "",
        name4: ""
    });
    const [fileCheck, setFileCheck] = useState({
            file1: false,
            file2: false,
            file3: false,
            file4: false
        }
    );
    const [count, setCount] = useState(0);
    let [genre, setGenre] = useState({
        Action: false,
        Drama: false,
        Comedy: false,
        Thriller: false,
        Horror: false,
        Crime: false,
        ScienceFiction: false
    });
    const [actor, setActor] = useState("");

    function handleAction(e) {
        let isChecked = e.target.checked;
        genre.Action = (isChecked === true);
    }

    function handleDrama(e) {
        let isChecked = e.target.checked;
        genre.Drama = (isChecked === true);
    }

    function handleComedy(e) {
        let isChecked = e.target.checked;
        genre.Comedy = (isChecked === true);
    }

    function handleThriller(e) {
        let isChecked = e.target.checked;
        genre.Thriller = (isChecked === true);
    }

    function handleHorror(e) {
        let isChecked = e.target.checked;
        genre.Horror = (isChecked === true);
    }

    function handleCrime(e) {
        let isChecked = e.target.checked;
        genre.Crime = (isChecked === true);
    }

    function handleSciFy(e) {
        let isChecked = e.target.checked;
        genre.ScienceFiction = (isChecked === true);
    }

    function submitShow(event) {
        event.preventDefault();
        console.log("Data present in Array is . ")
        let actorArray = actor.split(',')
        let genreArray = [];
        if (genre.Action === true) genreArray.push("Action");
        if (genre.Drama === true) genreArray.push("Drama");
        if (genre.Comedy === true) genreArray.push("Comedy");
        if (genre.Thriller === true) genreArray.push("Thriller");
        if (genre.Horror === true) genreArray.push("Horror");
        if (genre.Crime === true) genreArray.push("Crime");
        if (genre.ScienceFiction === true) genreArray.push("ScienceFiction");
        setData((data) => ({
            ...data,
            showCasts: actorArray,
            showGenre: genreArray,
            images: fileNames
        }))
        if (count === 0) {
            setCount(1)
            alert("Click Again To Submit Show")

        } else
            submitShowApi()
    }

    const submitShowApi = () => {
        // Get Last Record id from Api
        console.log(data);
        if (data.type === "" || data.name === "" || data.showCasts === ""
            || data.images === "" || data.description === "" ||
            data.showGenre === "") {
            alert("Some Field are Missing")
        }
        // Call Post Api.
        setCount(0);
    }
    const submitImage = () => {
        try {
            const fileInput = document.getElementById('file0');
            const fileName = fileInput.files[0].name;
            console.log(fileName);
        } catch (err) {
            alert("Invalid Action");
            return
        }

        if (fileCheck.file1 === true) {
            alert("Already Uploaded");
        } else {
            const fileInput = document.getElementById('file0');
            const fileName = fileInput.files[0].name;
            const fileType = fileInput.files[0].type;
            if (fileType === "image/png" || fileType === "image/jpeg") {
                setFilesNames((fileNames) => ({
                    ...fileNames,
                    name1: fileName
                }))

                console.log("File Uploaded Successfully")
                setFileCheck((fileCheck) => ({
                    ...fileCheck,
                    file1: true
                }))
            } else
                alert("Wrong Format use only .Jpeg or .Png");
        }
    }
    const submitImage1 = () => {
        try {
            const fileInput = document.getElementById('file0');
            const fileName = fileInput.files[0].name;
            console.log(fileName);
        } catch (err) {
            alert("Invalid Action");
            return
        }
        const fileInput = document.getElementById('file1');
        const fileName = fileInput.files[0].name;
        const fileType = fileInput.files[0].type;
        if (fileCheck.file2)
            alert(fileName + " is Already Uploaded");
        else {
            if (fileType === "image/png" || fileType === "image/jpeg") {
                setFileCheck((fileCheck) => ({
                    ...fileCheck,
                    file2: true
                }))
                // fileNames.push(fileName);
                setFilesNames((fileNames) => ({
                    ...fileNames,
                    name2: fileName
                }))
                console.log("File Uploaded Successfully")
            } else
                alert("Wrong Format use only .Jpeg or .Png");
        }

    }
    const submitImage2 = () => {
        try {
            const fileInput = document.getElementById('file0');
            const fileName = fileInput.files[0].name;
            console.log(fileName);
        } catch (err) {
            alert("Invalid Action");
            return
        }
        const fileInput = document.getElementById('file2');
        const fileName = fileInput.files[0].name;
        const fileType = fileInput.files[0].type;
        if (fileCheck.file4)
            alert(fileName + " is Already Uploaded");
        else {
            if (fileType === "image/png" || fileType === "image/jpeg") {
                setFileCheck((fileCheck) => ({
                    ...fileCheck,
                    file3: true
                }))
                // fileNames.push(fileName);
                setFilesNames((fileNames) => ({
                    ...fileNames,
                    name3: fileName
                }))
                console.log("File Uploaded Successfully")
            } else
                alert("Wrong Format use only .Jpeg or .Png");
        }
    }
    const submitImage3 = () => {
        try {
            const fileInput = document.getElementById('file0');
            const fileName = fileInput.files[0].name;
            console.log(fileName);
        } catch (err) {
            alert("Invalid Action");
            return
        }
        const fileInput = document.getElementById('file3');
        const fileName = fileInput.files[0].name;
        const fileType = fileInput.files[0].type;
        if (fileCheck.file4)
            alert(fileName + " is Already Uploaded");
        else {
            setFileCheck((fileCheck) => ({
                ...fileCheck,
                file4: true
            }))
            if (fileType === "image/png" || fileType === "image/jpeg") {
                // fileNames.push(fileName);
                setFilesNames((fileNames) => ({
                    ...fileNames,
                    name3: fileName
                }))
                console.log("File Uploaded Successfully")
            } else
                alert("Wrong Format use only .Jpeg or .Png");
        }
    }

    return (
        <div className={"upload"}>
            <form>
                <div className="form-row">
                    <div className="form-group col-md-3 text-light">
                        <label htmlFor="inputEmail4">Show Name</label>
                        <input type="text" className="form-control " placeholder="Show Name"
                               onChange={(event) => {
                                   setData((data) => ({
                                       ...data,
                                       name: event.target.value
                                   }))
                               }}
                        />
                    </div>

                </div>
                <div className="form-group text-light">
                    <label htmlFor="inputDescription">Description</label>
                    <textarea className="form-control  col-md-6" id="description" rows="3"
                              placeholder={"Enter Description of Show "}
                              onChange={(event) => {
                                  setData((data) => ({
                                      ...data,
                                      description: event.target.value
                                  }))
                              }}
                    ></textarea>
                </div>
                <div className="form-row text-light">
                    <div className="form-group col-md-2">
                        <label htmlFor="inputCity">Director Name</label>
                        <input type="text" className="form-control" id="Director"
                               placeholder={"Director Name"}
                               onChange={(event) => {
                                   setData((data) => ({
                                       ...data,
                                       directorName: event.target.value
                                   }))
                               }}
                        />
                    </div>
                    <div className="form-group col-md-2">
                        <label htmlFor="inputState">Type</label>
                        <select value={"DEFAULT"} id="Type" className="form-control"
                                onChange={(event) => {
                                    setData((data) => ({
                                        ...data,
                                        type: event.target.value
                                    }))
                                }}>
                            <option selected value="Choose">Choose...</option>
                            <option value="Movie">Movie</option>
                            <option value="Tv">Tv</option>
                        </select>
                    </div>
                    <div className="form-group col-md-2">
                        <label htmlFor="inputZip">Duration</label>
                        <input type="text" className="form-control" placeholder={"2 H 30 M"} id="inputZip"
                               onChange={(event) => {
                                   setData((data) => ({
                                       ...data,
                                       time: event.target.value
                                   }))
                               }}
                        />
                    </div>
                    <div className="form-group col-md-2">
                        <label htmlFor="inputZip">Ratings</label>
                        <input type="number" className="form-control" min={1} max={10} placeholder="1 to 10 "
                               id="inputZip"
                               onChange={(event) => {
                                   setData((data) => ({
                                       ...data,
                                       rating: event.target.value
                                   }))
                               }}
                        />
                    </div>
                </div>
                 {/*Actor Name Section.*/}
                <div className="form-row text-light">
                    <div className="form-group col-mid-3">
                        <label htmlFor="inputDescription">Actor's Name</label>
                        <textarea className="form-control  col-mid-3" id="exampleFormControlTextarea1" rows="2"
                                  placeholder={"Use ',' and space between Multiple Name . Ex Tom , Ajay"}
                                  onChange={(event) => {
                                      setActor(event.target.value);
                                  }}
                        ></textarea>
                    </div>
                </div>
                {/*Section For Show Genre Selection.*/}
                <div className="container-Genre text-light">
                    <label className={" form-row text-light "}>Add Genre for Show</label>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox"
                               onClick={handleAction} value={"Action"}/>
                        <label className="form-check-label">Action</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox"
                               onClick={handleDrama} value={"Drama"}/>
                        <label className="form-check-label">Drama</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox"
                               onClick={handleComedy} value={"Comedy"}/>
                        <label className="form-check-label">Comedy</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox"
                               onClick={handleThriller} value={"Thriller"}/>
                        <label className="form-check-label">Thriller</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox"
                               onClick={handleHorror} value={"Horror"}/>
                        <label className="form-check-label">Horror</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox"
                               onClick={handleCrime} value={"Crime"}/>
                        <label className="form-check-label">Crime</label>
                    </div>
                    <div className="form-check form-check-inline">
                        <input className="form-check-input" type="checkbox"
                               onClick={handleSciFy} value={"Science Fiction"}/>
                        <label className="form-check-label">Science Fiction</label>
                    </div>
                </div>
                {/*Show Images Section*/}
                <label className={"text-light"}>Add Images For Show </label>
                <div className="form-row text-light m-1">
                    <div className="form-row text-light my-5">
                        <input type="file" className="form-control-file" id={"file0"}/>
                        <button type="button" onClick={submitImage} className="btn btn-primary btn-sm my-2 si">Add img
                        </button>
                    </div>
                    <div className="form-group text-light my-5">
                        <input type="file" className="form-control-file" id={"file1"}/>
                        <button type="button" onClick={submitImage1} className="btn btn-primary btn-sm my-2">Add img
                        </button>
                    </div>
                    <div className="form-group text-light my-5">
                        <input type="file" className="form-control-file" id={"file2"}/>
                        <button type="button" onClick={submitImage2} className="btn btn-primary btn-sm my-2">Add img
                        </button>
                    </div>
                    <div className="form-group text-light my-5">
                        <input type="file" className="form-control-file" id={"file3"}/>
                        <button type="button" onClick={submitImage3} className="btn btn-primary btn-sm my-2">Add img
                        </button>
                    </div>
                </div>
                <button type="button" onClick={submitShow} className="btn btn-outline-success">Add Show</button>
            </form>
        </div>
    );
}

export default Upload;