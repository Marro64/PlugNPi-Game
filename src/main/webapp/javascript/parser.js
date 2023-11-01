/**
 * @param {String} html representing any number of sibling elements
 * @return {NodeList}
 */
const parseHTML = (html) => {
    var template = document.createElement("template");
    template.innerHTML = html;
    return template.content.firstChild;
}
