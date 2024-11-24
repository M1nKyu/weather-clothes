// Function to create the tabbed interface
function setupOutfitDisplayTabs() {
    const outfitDisplay = document.querySelector('.outfit-display');
    
    // Create tab container
    const tabContainer = document.createElement('div');
    tabContainer.classList.add('tab-container');
    
    // Create content container
    const contentContainer = document.createElement('div');
    contentContainer.classList.add('content-container');
    
    // Get main categories
    const mainCategories = document.querySelectorAll('.main-category-title');
    
    // Create tabs and content sections
    mainCategories.forEach((category, index) => {
        const categoryName = category.textContent;
        
        // Create tab
        const tab = document.createElement('button');
        tab.textContent = categoryName;
        tab.classList.add('tab');
        if (index === 0) tab.classList.add('active');
        
        // Create corresponding content section
        const contentSection = document.createElement('div');
        contentSection.classList.add('category-content');
        if (index !== 0) contentSection.style.display = 'none';
        
        // Find subcategories for this main category
        const subCategories = category.closest('.product-categories')
            .querySelectorAll('.sub-category-title');
        
        // Create subcategory tabs
        const subTabContainer = document.createElement('div');
        subTabContainer.classList.add('sub-tab-container');
        
        // Create subcategory content containers
        const subContentContainer = document.createElement('div');
        subContentContainer.classList.add('sub-content-container');
        
        subCategories.forEach((subCategory, subIndex) => {
            const subCategoryName = subCategory.textContent;
            
            // Subcategory tab
            const subTab = document.createElement('button');
            subTab.textContent = subCategoryName;
            subTab.classList.add('sub-tab');
            if (subIndex === 0) subTab.classList.add('active');
            
            // Subcategory content
            const subContentSection = document.createElement('div');
            subContentSection.classList.add('sub-category-content');
            if (subIndex !== 0) subContentSection.style.display = 'none';
            
            // Find product list for this subcategory
            const productList = subCategory.closest('.category-products')
                .querySelector('.product-list');
            
            // Clone and add product list to subcategory content
            subContentSection.appendChild(productList.cloneNode(true));
            
            subTabContainer.appendChild(subTab);
            subContentContainer.appendChild(subContentSection);
            
            // Subcategory tab click handler
            subTab.addEventListener('click', () => {
                // Remove active class from all subcategory tabs
                subTabContainer.querySelectorAll('.sub-tab')
                    .forEach(t => t.classList.remove('active'));
                subTab.classList.add('active');
                
                // Hide all subcategory content
                subContentContainer.querySelectorAll('.sub-category-content')
                    .forEach(c => c.style.display = 'none');
                
                // Show selected subcategory content
                subContentSection.style.display = 'flex';
            });
        });
        
        contentSection.appendChild(subTabContainer);
        contentSection.appendChild(subContentContainer);
        
        tabContainer.appendChild(tab);
        contentContainer.appendChild(contentSection);
        
        // Main category tab click handler
        tab.addEventListener('click', () => {
            // Remove active class from all tabs
            tabContainer.querySelectorAll('.tab')
                .forEach(t => t.classList.remove('active'));
            tab.classList.add('active');
            
            // Hide all content sections
            contentContainer.querySelectorAll('.category-content')
                .forEach(c => c.style.display = 'none');
            
            // Show selected content section
            contentSection.style.display = 'block';
        });
    });
    
    // Clear existing content and add new structure
    outfitDisplay.innerHTML = '';
    outfitDisplay.appendChild(tabContainer);
    outfitDisplay.appendChild(contentContainer);
}

// Call the function when the page loads
document.addEventListener('DOMContentLoaded', setupOutfitDisplayTabs);