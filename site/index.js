function styleSiteNavMenuAsStackOrCluster(mediaQuery) {
    const siteNavMenuEle = document.querySelector('.site-nav > ul');
    if (mediaQuery.matches) {
        siteNavMenuEle.classList.remove('stack');
        siteNavMenuEle.classList.add('cluster');
    } else {
        siteNavMenuEle.classList.remove('cluster');
        siteNavMenuEle.classList.add('stack');
    }
}

function stretchOrShrinkSidebar(mediaQuery) {
    const mainSidebar = document.querySelector('.with-sidebar');
    if (mediaQuery.matches) {
        mainSidebar.style.minBlockSize = 'initial';
    } else {
        mainSidebar.style.minBlockSize = '100%';
    }
}

function applyMediaQueryChange(mediaQuery) {
    styleSiteNavMenuAsStackOrCluster(mediaQuery);
    stretchOrShrinkSidebar(mediaQuery);
}

function setupMediaQueryChange() {
    const mediaQuery = window.matchMedia('(width <= 80ch)');
    applyMediaQueryChange(mediaQuery);
    mediaQuery.addEventListener('change', applyMediaQueryChange);
}

function addListRoleToTimelines() {
    // To fix Safari's decision to remove list role from
    // those lists which have `list-style-type: none` set in CSS:
    // https://www.scottohara.me/blog/2019/01/12/lists-and-safari.html
    const nodes = document.querySelectorAll('.timeline');
    for (const node of nodes) {
        node.setAttribute('role', 'list');
    }
}

document.addEventListener('DOMContentLoaded', function () {
    setupMediaQueryChange();
    addListRoleToTimelines();
});