

console.log("script loaded")

let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});

function changeTheme() {
    // set to web page
    changePageTheme(currentTheme, "");
    // set the listener to chnage theme button
    const changeThemeButton = document.querySelector("#theme_change_button");
    changeThemeButton.addEventListener("click", (event) => {
        let oldTheme = currentTheme;
        if (currentTheme == "dark") {
            // change theme to light
            currentTheme = "light";
        }
        else {
            // change theme to dark
            currentTheme = "dark";
        }

        changePageTheme(currentTheme, oldTheme);
    });
}


// set theme to local storage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

// get theme from local storage

function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}


// change current page theme
function changePageTheme(theme, oldTheme) {
    // update in local storage
    setTheme(currentTheme);
    // remove the current theme 
    if (oldTheme) {
        document.querySelector("html").classList.remove(oldTheme);
    }

    // set the current theme
    document.querySelector('html').classList.add(theme);

    // change the text of button
    document.querySelector('#theme_change_button')
        .querySelector("span").textContent = theme == "light" ? "Dark" : "Light";

}