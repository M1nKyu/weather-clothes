function scrollToSectionByClass(className) {
    const elements = document.getElementsByClassName(className);
    if (elements.length > 0) {
        elements[0].scrollIntoView({ behavior: 'smooth' });
    } else {
        console.error(`No elements with class '${className}' found.`);
    }
}