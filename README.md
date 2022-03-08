# FreelancerAnalyser
##Coding standards
 to follow https://google.github.io/styleguide/javaguide.html

## Branch Naming
All characters are lowercase, split each word with '-'

**Feature - For any feature**  
feature/#short-description, Ex: feature/search-bar-interface 

**Test : Test branch for anything that don't need to be merged**  
test/short-task-description, Ex: test/button-on-right

## Commit message rules  
- First word of the commit must be a imperative verb
- The subject line must start with a capitalized character  

## Merge Request/Review Checklist
Always rebase with develop before creating the pull request

### Code
Before pull request :
- If a function seems to be too big, try to split it in multiple functions
- Check console in your IDE for errors (0 errors)
- Check console in your IDE for warnings (0 warnings)

Review :
- git Checkout the branch
- Check console for errors (0 errors)
- Check console for warnings (0 warnings)
- Review the code functionality and naming
- Verify if the pipeline passes for the branch
