# Release Guide

## Creating a Release

1. **Go to GitHub**: Navigate to https://github.com/modelcontextprotocol/registry/releases
2. **Click "Draft a new release"**
3. **Choose a tag**: Click "Choose a tag" and type a new semantic version that follows the last one available (e.g., `v1.0.0`)
5. **Generate notes**: Click "Generate release notes" to auto-populate the name and description
6. **Publish**: Click "Publish release"

The release workflow will automatically:
- Build binaries for 6 platforms (Linux, macOS, Windows × amd64, arm64)
- Create and push Docker images with `:latest` and `:vX.Y.Z` tags
- Attach all artifacts to the GitHub release
- Generate checksums and signatures

## After Release

The release workflow will automatically:
- Build and publish Docker images:
  - `ghcr.io/modelcontextprotocol/registry:latest` - Latest stable release
  - `ghcr.io/modelcontextprotocol/registry:vX.Y.Z` - Specific release version
- Binaries will be available on the GitHub release page

**Important:** Creating a release does **not** automatically deploy to production. Production deployments are triggered by pushes to the `main` branch.

## Deploying the Release to Production

After creating a release, you need to ensure production pulls the new `:latest` Docker image. You have two options:

1. **Recommended:** Merge a commit to `main` (e.g., a changelog update or version bump). This will trigger the deployment workflow which will:
   - Build and push a new `:main` Docker image
   - Deploy to staging
   - Deploy to production (which uses `:latest` and will pull the new image)

2. **Manual restart (requires kubectl access):**
   ```bash
   kubectl rollout restart deployment/mcp-registry -n default
   ```

## Verifying the Deployment

After deployment completes, verify the changes are live:
- Check the deployment workflow succeeded: https://github.com/modelcontextprotocol/registry/actions/workflows/deploy.yml
- Test the API endpoint to confirm schema changes are applied
- Check migrations ran successfully in the pod logs

## Docker Image Tags

The registry publishes different Docker image tags for different use cases:

- **`:latest`** - Latest stable release (updated only on releases)
- **`:vX.Y.Z`** - Specific release versions (e.g., `:v1.0.0`)
- **`:main`** - Rolling tag updated on every push to main branch (continuous deployment)
- **`:main-YYYYMMDD-sha`** - Specific development builds from main branch

## Versioning

We use semantic versioning (SemVer):
- `v1.0.0` - Major release with breaking changes
- `v1.1.0` - Minor release with new features
- `v1.0.1` - Patch release with bug fixes