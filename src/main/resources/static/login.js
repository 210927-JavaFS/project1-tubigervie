const URL = "http://localhost:8081/";

const loginButton = document.getElementById("login-form-submit");
const errorMessage = document.getElementById("error-message");

loginButton.addEventListener("click", login);

sessionStorage.clear();

async function login()
{
    let name = document.getElementById("InputUsername").value;
    let pass = document.getElementById("InputPassword").value;
    if(name == null || name=="" || pass == null || pass == null)
    {
        errorMessage.innerText = "Username/password cannot be left blank.";
        errorMessage.hidden = false;
        return;
    }

    let user = {username: name, password: pass};
    errorMessage.hidden = true;
    console.log(user);

    let response = await fetch(URL + "login", 
    {
        method: "POST",
        body: JSON.stringify(user),
        credentials: "include"
    });

    if(response.status === 200)
    {
        let data = await response.json();
        sessionStorage.setItem("currentLoginName", data.username);
        sessionStorage.setItem("currentLoginID", data.id);
        console.log("Login successful.");
        if(data["userRole"] == "MANAGER")
            window.location.href = "managermenu.html?"+name;
        else
            window.location.href = "employeemenu.html?"+name;
    }
    else
    {
        console.log("Login failed.");
        errorMessage.innerText = "Login failed.";
        errorMessage.hidden = false;
    }
}